package com.crewcomms.watch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.core.model.WatchState
import com.crewcomms.watch.data.WatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class WatchViewModel @Inject constructor(
    private val repository: WatchRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(WatchState())
    val state: StateFlow<WatchState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.watchState.collectLatest { _state.value = it }
        }
    }

    fun sendCommand(command: QuickCommand) {
        viewModelScope.launch {
            repository.sendQuickCommand(command)
        }
    }
}
