package com.crewcomms.phone.data

import com.crewcomms.core.database.repository.HistoryRepository
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.DeviceRole
import com.crewcomms.core.model.MessageType
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.core.model.TransportEvent
import com.crewcomms.core.model.WatchMessagePreview
import com.crewcomms.core.model.WatchState
import com.crewcomms.core.transport.CrewTransport
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ViewModelScoped
class CrewSessionRepository @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val preferencesStore: PhonePreferencesStore,
    private val wearSyncManager: WearSyncManager,
    private val sessionServiceController: SessionServiceController,
    @Named("mock") private val mockTransport: CrewTransport,
    @Named("nearby") private val nearbyTransport: CrewTransport,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentRoom = MutableStateFlow<CrewRoom?>(null)
    private val _nearbyCrews = MutableStateFlow<List<NearbyCrew>>(emptyList())
    private val _members = MutableStateFlow<List<CrewMember>>(emptyList())
    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private var currentTransport: CrewTransport = mockTransport
    private var transportObserverJob: Job? = null

    val currentRoom: StateFlow<CrewRoom?> = _currentRoom.asStateFlow()
    val nearbyCrews: StateFlow<List<NearbyCrew>> = _nearbyCrews.asStateFlow()
    val members: StateFlow<List<CrewMember>> = _members.asStateFlow()
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()
    val settings: StateFlow<PhoneSettings> = preferencesStore.settings.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = PhoneSettings(),
    )

    val messages = currentRoom.flatMapLatest { room ->
        if (room == null) {
            kotlinx.coroutines.flow.flowOf(emptyList())
        } else {
            historyRepository.observeMessages(room.id)
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    init {
        scope.launch {
            settings.collectLatest { prefs ->
                val nextTransport = if (prefs.useMockTransport) mockTransport else nearbyTransport
                if (nextTransport !== currentTransport) {
                    currentTransport.stop()
                    currentTransport = nextTransport
                    observeCurrentTransport()
                }
            }
        }

        observeCurrentTransport()

        scope.launch {
            WatchCommandBus.commands.collectLatest { command ->
                sendQuickCommand(command.command, senderOverride = command.senderDeviceName)
            }
        }

        scope.launch {
            combine(messages, currentRoom, members, connectionStatus) { messagesList, room, memberList, status ->
                WatchState(
                    roomId = room?.id,
                    roomName = room?.name,
                    connectionStatus = status,
                    lastMessages = messagesList
                        .sortedByDescending { it.timestamp }
                        .take(5)
                        .map {
                            WatchMessagePreview(
                                senderName = it.senderName,
                                body = it.body,
                                timestamp = it.timestamp,
                            )
                        },
                    memberCount = memberList.size,
                    lastEventTimestamp = System.currentTimeMillis(),
                )
            }.collectLatest { wearSyncManager.publishWatchState(it) }
        }
    }

    private fun observeCurrentTransport() {
        transportObserverJob?.cancel()
        transportObserverJob = scope.launch {
            launch {
                currentTransport.connectionStatus.collectLatest { status ->
                    _connectionStatus.value = status
                    syncWatchState()
                }
            }

            launch {
                currentTransport.events.collectLatest { event ->
                    when (event) {
                        is TransportEvent.CrewDiscovered -> {
                            _nearbyCrews.update { existing ->
                                (existing.filterNot { it.endpointId == event.crew.endpointId } + event.crew)
                                    .sortedBy { it.roomName }
                            }
                        }

                        is TransportEvent.CrewLost -> {
                            _nearbyCrews.update { crews -> crews.filterNot { it.endpointId == event.endpointId } }
                        }

                        is TransportEvent.MemberJoined -> {
                            _members.update { members ->
                                (members.filterNot { it.id == event.member.id } + event.member)
                                    .sortedBy { it.displayName }
                            }
                            historyRepository.saveMember(event.member)
                            syncWatchState()
                        }

                        is TransportEvent.MemberLeft -> {
                            _members.update { list -> list.filterNot { it.id == event.memberId } }
                            syncWatchState()
                        }

                        is TransportEvent.MessageReceived -> {
                            if (event.message.roomId == _currentRoom.value?.id) {
                                historyRepository.saveMessage(event.message)
                                syncWatchState()
                            }
                        }

                        is TransportEvent.ConnectionError -> {
                            _connectionStatus.value = ConnectionStatus.ERROR
                            addSystemMessage(event.reason)
                        }
                    }
                }
            }
        }
    }

    suspend fun createCrew(name: String, pinRequired: Boolean, pin: String?) {
        val displayName = settings.value.displayName
        val room = CrewRoom(
            id = UUID.randomUUID().toString(),
            name = name.ifBlank { "Crew ${System.currentTimeMillis() % 1000}" },
            hostName = displayName,
            pinRequired = pinRequired,
            pinCode = if (pinRequired) pin else null,
        )
        _currentRoom.value = room

        val self = CrewMember(
            id = "self",
            displayName = displayName,
            endpointId = null,
            role = DeviceRole.CAPTAIN,
        )
        _members.value = listOf(self)
        historyRepository.saveMember(self)
        addSystemMessage("Crew channel ${room.name} opened")
        currentTransport.startAdvertising(room)
        sessionServiceController.start()
        syncWatchState()
    }

    suspend fun discoverCrews() {
        _nearbyCrews.value = emptyList()
        currentTransport.startDiscovery()
    }

    suspend fun joinCrew(crew: NearbyCrew, pin: String?) {
        val selfMember = CrewMember(
            id = "self",
            displayName = settings.value.displayName,
            endpointId = null,
            role = DeviceRole.CREW,
        )
        _members.value = listOf(selfMember)
        historyRepository.saveMember(selfMember)

        _currentRoom.value = CrewRoom(
            id = crew.roomId,
            name = crew.roomName,
            hostName = crew.hostName,
            hostEndpointId = crew.endpointId,
            pinRequired = crew.pinRequired,
            pinCode = pin,
        )
        currentTransport.connectToCrew(crew.endpointId, pin)
        addSystemMessage("Joined ${crew.roomName}")
        sessionServiceController.start()
        syncWatchState()
    }

    suspend fun sendTextMessage(text: String) {
        val room = _currentRoom.value ?: return
        if (text.isBlank()) return

        val message = CrewMessage(
            id = UUID.randomUUID().toString(),
            roomId = room.id,
            senderId = "self",
            senderName = settings.value.displayName,
            body = text,
            type = MessageType.TEXT,
        )

        historyRepository.saveMessage(message)
        currentTransport.sendMessage(message)
        syncWatchState()
    }

    suspend fun sendQuickCommand(command: QuickCommand, senderOverride: String? = null) {
        val room = _currentRoom.value ?: return
        val senderName = senderOverride ?: settings.value.displayName
        val body = when (command) {
            QuickCommand.PING -> "PING"
            QuickCommand.HELP -> "HELP"
            QuickCommand.READY -> "READY"
            QuickCommand.COME_HERE -> "COME HERE"
            QuickCommand.WAIT -> "WAIT"
        }

        val message = CrewMessage(
            id = UUID.randomUUID().toString(),
            roomId = room.id,
            senderId = "self",
            senderName = senderName,
            body = body,
            type = MessageType.QUICK_COMMAND,
            quickCommand = command,
        )

        historyRepository.saveMessage(message)
        currentTransport.sendMessage(message)
        syncWatchState()
    }

    suspend fun sendVoicePlaceholder() {
        val room = _currentRoom.value ?: return
        val message = CrewMessage(
            id = UUID.randomUUID().toString(),
            roomId = room.id,
            senderId = "self",
            senderName = settings.value.displayName,
            body = "Voice note placeholder sent",
            type = MessageType.VOICE_NOTE_PLACEHOLDER,
        )
        historyRepository.saveMessage(message)
        currentTransport.sendMessage(message)
    }

    suspend fun clearHistory() {
        historyRepository.clearAll()
    }

    suspend fun leaveCrew() {
        currentTransport.stop()
        _currentRoom.value = null
        _members.value = emptyList()
        _nearbyCrews.value = emptyList()
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        sessionServiceController.stop()
        syncWatchState()
    }

    suspend fun updateDisplayName(name: String) = preferencesStore.updateDisplayName(name)
    suspend fun setVibrationRelay(enabled: Boolean) = preferencesStore.updateVibrationRelay(enabled)
    suspend fun setKeepScreenAwake(enabled: Boolean) = preferencesStore.updateKeepScreenAwake(enabled)
    suspend fun setAutoReconnect(enabled: Boolean) = preferencesStore.updateAutoReconnect(enabled)
    suspend fun setUseMock(enabled: Boolean) = preferencesStore.updateUseMock(enabled)

    private suspend fun addSystemMessage(text: String) {
        val room = _currentRoom.value ?: return
        historyRepository.saveMessage(
            CrewMessage(
                id = UUID.randomUUID().toString(),
                roomId = room.id,
                senderId = "system",
                senderName = "Signal",
                body = text,
                type = MessageType.SYSTEM,
            )
        )
    }

    private fun syncWatchState() {
        val room = _currentRoom.value
        wearSyncManager.publishWatchState(
            WatchState(
                roomId = room?.id,
                roomName = room?.name,
                connectionStatus = _connectionStatus.value,
                memberCount = _members.value.size,
                lastEventTimestamp = System.currentTimeMillis(),
            )
        )
    }
}
