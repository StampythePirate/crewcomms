package com.crewcomms.phone.data

import com.crewcomms.core.model.WatchCommand
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object WatchCommandBus {
    private val _commands = MutableSharedFlow<WatchCommand>(extraBufferCapacity = 32)
    val commands = _commands.asSharedFlow()

    fun tryEmit(command: WatchCommand) {
        _commands.tryEmit(command)
    }
}
