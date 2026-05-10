package com.crewcomms.core.transport

import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.DeviceRole
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.core.model.TransportEvent
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Singleton
class LocalLanTransport @Inject constructor() : CrewTransport {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val selfId = UUID.randomUUID().toString()

    private val _events = MutableSharedFlow<TransportEvent>(extraBufferCapacity = 128)
    private val _status = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private val discoveredCrews = ConcurrentHashMap<String, NearbyCrew>()
    private var currentRoom: CrewRoom? = null

    private var socket: DatagramSocket? = null
    private var receiverJob: Job? = null
    private var announceJob: Job? = null

    override val events: Flow<TransportEvent> = _events.asSharedFlow()
    override val connectionStatus: Flow<ConnectionStatus> = _status.asStateFlow()

    override suspend fun startAdvertising(room: CrewRoom) {
        currentRoom = room
        ensureSocket()
        _status.value = ConnectionStatus.CONNECTED
        startRoomAnnouncements(room)
    }

    override suspend fun startDiscovery() {
        ensureSocket()
        _status.value = ConnectionStatus.DISCOVERING
    }

    override suspend fun stop() {
        announceJob?.cancel()
        announceJob = null
        receiverJob?.cancel()
        receiverJob = null
        socket?.close()
        socket = null
        discoveredCrews.clear()
        _status.value = ConnectionStatus.DISCONNECTED
    }

    override suspend fun connectToCrew(endpointId: String, pin: String?) {
        val crew = discoveredCrews[endpointId]
        if (crew == null) {
            _events.emit(TransportEvent.ConnectionError("Crew endpoint not found."))
            _status.value = ConnectionStatus.ERROR
            return
        }

        if (crew.pinRequired && pin.isNullOrBlank()) {
            _events.emit(TransportEvent.ConnectionError("PIN required for this crew."))
            _status.value = ConnectionStatus.ERROR
            return
        }

        currentRoom = CrewRoom(
            id = crew.roomId,
            name = crew.roomName,
            hostName = crew.hostName,
            hostEndpointId = crew.endpointId,
            pinRequired = crew.pinRequired,
            pinCode = pin,
        )
        _status.value = ConnectionStatus.CONNECTED
        _events.emit(
            TransportEvent.MemberJoined(
                CrewMember(
                    id = endpointId,
                    displayName = crew.hostName,
                    endpointId = endpointId,
                    role = DeviceRole.CAPTAIN,
                )
            )
        )
    }

    override suspend fun sendMessage(message: CrewMessage) {
        val room = currentRoom
        if (room != null && room.id != message.roomId) return

        val payload = TransportPayload.CrewMessagePayload(
            message = message,
            sourceId = selfId,
        )
        sendPayload(payload)
    }

    private fun ensureSocket() {
        if (socket != null) return

        val datagramSocket = DatagramSocket(null).apply {
            reuseAddress = true
            broadcast = true
            soTimeout = 0
            bind(InetSocketAddress(PORT))
        }
        socket = datagramSocket

        receiverJob = scope.launch {
            val buffer = ByteArray(MAX_PACKET_SIZE)
            while (isActive) {
                try {
                    val packet = DatagramPacket(buffer, buffer.size)
                    datagramSocket.receive(packet)
                    handleIncoming(packet)
                } catch (_: Throwable) {
                    if (!isActive) break
                }
            }
        }
    }

    private fun startRoomAnnouncements(room: CrewRoom) {
        announceJob?.cancel()
        announceJob = scope.launch {
            while (isActive) {
                val payload = TransportPayload.RoomAnnouncement(
                    room = room,
                    sourceId = selfId,
                )
                sendPayload(payload)
                delay(ANNOUNCE_INTERVAL_MS)
            }
        }
    }

    private fun handleIncoming(packet: DatagramPacket) {
        val message = runCatching {
            val data = packet.data.copyOfRange(packet.offset, packet.offset + packet.length)
            CrewJson.decode<TransportPayload>(String(data, StandardCharsets.UTF_8))
        }.getOrNull() ?: return

        when (message) {
            is TransportPayload.RoomAnnouncement -> {
                if (message.sourceId == selfId) return

                val endpointId = "${packet.address.hostAddress}:${packet.port}:${message.room.id}"
                val crew = NearbyCrew(
                    endpointId = endpointId,
                    roomId = message.room.id,
                    roomName = message.room.name,
                    hostName = message.room.hostName,
                    pinRequired = message.room.pinRequired,
                    signalStrengthHint = 70,
                )

                discoveredCrews[endpointId] = crew
                scope.launch { _events.emit(TransportEvent.CrewDiscovered(crew)) }
            }

            is TransportPayload.CrewMessagePayload -> {
                if (message.sourceId == selfId) return

                val roomId = currentRoom?.id
                if (roomId != null && message.message.roomId != roomId) return
                scope.launch {
                    _status.value = ConnectionStatus.CONNECTED
                    _events.emit(TransportEvent.MessageReceived(message.message))
                }
            }
        }
    }

    private suspend fun sendPayload(payload: TransportPayload) {
        val packetBytes = CrewJson.encode(payload).toByteArray(StandardCharsets.UTF_8)
        val target = DatagramPacket(
            packetBytes,
            packetBytes.size,
            InetAddress.getByName(BROADCAST_ADDRESS),
            PORT,
        )
        socket?.send(target)
    }

    private companion object {
        private const val PORT = 44555
        private const val MAX_PACKET_SIZE = 8192
        private const val ANNOUNCE_INTERVAL_MS = 1600L
        private const val BROADCAST_ADDRESS = "255.255.255.255"
    }
}
