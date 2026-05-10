package com.crewcomms.watch.data

import com.crewcomms.core.model.WatchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object WatchStateBus {
    private val _state = MutableStateFlow(WatchState())
    val state = _state.asStateFlow()

    fun update(newState: WatchState) {
        _state.value = newState
    }
}
