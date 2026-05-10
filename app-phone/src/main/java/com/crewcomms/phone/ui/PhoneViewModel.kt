package com.crewcomms.phone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.phone.data.CrewSessionRepository
import com.crewcomms.phone.data.PhoneSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PhoneUiState(
    val room: CrewRoom? = null,
    val nearbyCrews: List<NearbyCrew> = emptyList(),
    val members: List<CrewMember> = emptyList(),
    val messages: List<CrewMessage> = emptyList(),
    val status: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val settings: PhoneSettings = PhoneSettings(),
)

@HiltViewModel
class PhoneViewModel @Inject constructor(
    private val sessionRepository: CrewSessionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PhoneUiState())
    val uiState: StateFlow<PhoneUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                sessionRepository.currentRoom.collectLatest { room ->
                    _uiState.update { it.copy(room = room) }
                }
            }
            launch {
                sessionRepository.nearbyCrews.collectLatest { crews ->
                    _uiState.update { it.copy(nearbyCrews = crews) }
                }
            }
            launch {
                sessionRepository.members.collectLatest { members ->
                    _uiState.update { it.copy(members = members) }
                }
            }
            launch {
                sessionRepository.messages.collectLatest { messages ->
                    _uiState.update { it.copy(messages = messages.sortedBy { message -> message.timestamp }) }
                }
            }
            launch {
                sessionRepository.connectionStatus.collectLatest { status ->
                    _uiState.update { it.copy(status = status) }
                }
            }
            launch {
                sessionRepository.settings.collectLatest { settings ->
                    _uiState.update { it.copy(settings = settings) }
                }
            }
        }
    }

    fun createCrew(name: String, pinRequired: Boolean, pin: String?) {
        viewModelScope.launch { sessionRepository.createCrew(name, pinRequired, pin) }
    }

    fun discoverNearbyCrews() {
        viewModelScope.launch { sessionRepository.discoverCrews() }
    }

    fun joinCrew(crew: NearbyCrew, pin: String?) {
        viewModelScope.launch { sessionRepository.joinCrew(crew, pin) }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch { sessionRepository.sendTextMessage(text) }
    }

    fun sendQuickCommand(command: QuickCommand) {
        viewModelScope.launch { sessionRepository.sendQuickCommand(command) }
    }

    fun sendVoicePlaceholder() {
        viewModelScope.launch { sessionRepository.sendVoicePlaceholder() }
    }

    fun clearHistory() {
        viewModelScope.launch { sessionRepository.clearHistory() }
    }

    fun leaveCrew() {
        viewModelScope.launch { sessionRepository.leaveCrew() }
    }

    fun updateDisplayName(name: String) {
        viewModelScope.launch { sessionRepository.updateDisplayName(name) }
    }

    fun setVibrationRelay(enabled: Boolean) {
        viewModelScope.launch { sessionRepository.setVibrationRelay(enabled) }
    }

    fun setKeepScreenAwake(enabled: Boolean) {
        viewModelScope.launch { sessionRepository.setKeepScreenAwake(enabled) }
    }

    fun setAutoReconnect(enabled: Boolean) {
        viewModelScope.launch { sessionRepository.setAutoReconnect(enabled) }
    }

    fun setUseMock(enabled: Boolean) {
        viewModelScope.launch { sessionRepository.setUseMock(enabled) }
    }
}
