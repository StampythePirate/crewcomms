package com.crewcomms.core.transport

import android.content.Context
import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.DeviceRole
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.core.model.TransportEvent
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Singleton
class NearbyConnectionsTransport @Inject constructor(
    @ApplicationContext context: Context,
) : CrewTransport {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(context)
    private val connectedEndpoints = ConcurrentHashMap.newKeySet<String>()

    private val _events = MutableSharedFlow<TransportEvent>(extraBufferCapacity = 128)
    private val _status = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private var currentRoom: CrewRoom? = null

    override val events: Flow<TransportEvent> = _events.asSharedFlow()
    override val connectionStatus: Flow<ConnectionStatus> = _status.asStateFlow()

    override suspend fun startAdvertising(room: CrewRoom) {
        currentRoom = room
        _status.value = ConnectionStatus.ADVERTISING

        val options = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        val endpointName = room.hostName

        connectionsClient.startAdvertising(
            endpointName,
            SERVICE_ID,
            connectionLifecycleCallback,
            options,
        ).await()

        // TODO: v1.1 broadcast richer metadata and secure handshake (PIN + signed nonce).
    }

    override suspend fun startDiscovery() {
        _status.value = ConnectionStatus.DISCOVERING
        val options = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        connectionsClient.startDiscovery(SERVICE_ID, endpointDiscoveryCallback, options).await()
    }

    override suspend fun stop() {
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
        connectionsClient.stopAllEndpoints()
        connectedEndpoints.clear()
        _status.value = ConnectionStatus.DISCONNECTED
    }

    override suspend fun connectToCrew(endpointId: String, pin: String?) {
        _status.value = ConnectionStatus.CONNECTING
        connectionsClient.requestConnection(
            DEVICE_NAME_PREFIX,
            endpointId,
            connectionLifecycleCallback,
        ).await()
        // TODO: validate PIN through explicit auth payload before sending regular traffic.
    }

    override suspend fun sendMessage(message: CrewMessage) {
        val roomId = currentRoom?.id
        if (roomId != null && message.roomId != roomId) return

        val payload = CrewJson.encode(TransportPayload.CrewMessagePayload(message))
            .toByteArray(StandardCharsets.UTF_8)

        connectedEndpoints.forEach { endpointId ->
            connectionsClient.sendPayload(endpointId, Payload.fromBytes(payload))
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            scope.launch {
                _events.emit(
                    TransportEvent.CrewDiscovered(
                        NearbyCrew(
                            endpointId = endpointId,
                            roomId = endpointId,
                            roomName = info.endpointName,
                            hostName = info.endpointName,
                            pinRequired = false,
                            signalStrengthHint = null,
                        )
                    )
                )
            }
        }

        override fun onEndpointLost(endpointId: String) {
            scope.launch { _events.emit(TransportEvent.CrewLost(endpointId)) }
        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            connectionsClient.acceptConnection(endpointId, payloadCallback)
            scope.launch {
                _events.emit(
                    TransportEvent.MemberJoined(
                        CrewMember(
                            id = endpointId,
                            displayName = connectionInfo.endpointName,
                            endpointId = endpointId,
                            role = DeviceRole.CREW,
                        )
                    )
                )
            }
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                connectedEndpoints.add(endpointId)
                _status.value = ConnectionStatus.CONNECTED
            } else {
                _status.value = ConnectionStatus.ERROR
                scope.launch {
                    _events.emit(TransportEvent.ConnectionError("Failed to connect: ${result.status.statusCode}"))
                }
            }
        }

        override fun onDisconnected(endpointId: String) {
            connectedEndpoints.remove(endpointId)
            if (connectedEndpoints.isEmpty()) _status.value = ConnectionStatus.DISCONNECTED
            scope.launch { _events.emit(TransportEvent.MemberLeft(endpointId)) }
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val bytes = payload.asBytes() ?: return
            val decoded = runCatching {
                CrewJson.decode<TransportPayload>(bytes.toString(StandardCharsets.UTF_8))
            }.getOrNull() ?: return

            when (decoded) {
                is TransportPayload.CrewMessagePayload -> {
                    val roomId = currentRoom?.id
                    if (roomId != null && decoded.message.roomId != roomId) return
                    scope.launch { _events.emit(TransportEvent.MessageReceived(decoded.message)) }
                }

                is TransportPayload.RoomAnnouncement -> {
                    // TODO: map room announcements into discovered crews and pin metadata.
                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit
    }

    companion object {
        private const val SERVICE_ID = "com.crewcomms.nearby.SERVICE"
        private const val DEVICE_NAME_PREFIX = "CrewComms"
    }
}
