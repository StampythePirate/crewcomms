package com.crewcomms.core.transport

import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.DeviceRole
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.core.model.TransportEvent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Singleton
class MockTransport @Inject constructor() : CrewTransport {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _events = MutableSharedFlow<TransportEvent>(extraBufferCapacity = 64)
    private val _status = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private var activeRoom: CrewRoom? = null

    override val events: Flow<TransportEvent> = _events.asSharedFlow()
    override val connectionStatus: Flow<ConnectionStatus> = _status.asStateFlow()

    override suspend fun startAdvertising(room: CrewRoom) {
        activeRoom = room
        _status.value = ConnectionStatus.ADVERTISING

        scope.launch {
            delay(600)
            _events.emit(
                TransportEvent.MemberJoined(
                    CrewMember(
                        id = UUID.randomUUID().toString(),
                        displayName = "Nearby Scout",
                        endpointId = "mock-endpoint-1",
                        role = DeviceRole.CREW,
                    )
                )
            )
        }
    }

    override suspend fun startDiscovery() {
        _status.value = ConnectionStatus.DISCOVERING
        scope.launch {
            delay(450)
            _events.emit(
                TransportEvent.CrewDiscovered(
                    NearbyCrew(
                        endpointId = "mock-crew-1",
                        roomId = "room-mock-1",
                        roomName = "Mock Captain Team",
                        hostName = "Captain Mock",
                        pinRequired = false,
                        signalStrengthHint = 82,
                    )
                )
            )
        }
    }

    override suspend fun stop() {
        _status.value = ConnectionStatus.DISCONNECTED
    }

    override suspend fun connectToCrew(endpointId: String, pin: String?) {
        _status.value = ConnectionStatus.CONNECTING
        delay(550)
        _status.value = ConnectionStatus.CONNECTED
        _events.emit(
            TransportEvent.MemberJoined(
                CrewMember(
                    id = endpointId,
                    displayName = "Captain Node",
                    endpointId = endpointId,
                    role = DeviceRole.CAPTAIN,
                )
            )
        )
    }

    override suspend fun sendMessage(message: CrewMessage) {
        if (activeRoom != null && message.roomId != activeRoom?.id) return
        _events.emit(TransportEvent.MessageReceived(message))

        scope.launch {
            delay(700)
            _events.emit(
                TransportEvent.MessageReceived(
                    message.copy(
                        id = UUID.randomUUID().toString(),
                        senderId = "mock-peer",
                        senderName = "Echo Mate",
                        body = "Ack: ${message.body.take(48)}",
                        timestamp = System.currentTimeMillis(),
                    )
                )
            )
        }
    }
}
