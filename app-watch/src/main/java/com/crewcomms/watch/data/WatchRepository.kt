package com.crewcomms.watch.data

import com.crewcomms.core.model.QuickCommand
import com.crewcomms.core.model.WatchState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow

@Singleton
class WatchRepository @Inject constructor(
    private val commandSender: WatchCommandSender,
) {
    val watchState: StateFlow<WatchState> = WatchStateBus.state

    suspend fun sendQuickCommand(command: QuickCommand) {
        commandSender.sendCommand(command = command, roomId = watchState.value.roomId)
    }
}
