package com.crewcomms.watch.data

import android.content.Context
import android.os.Build
import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.core.model.WatchCommand
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class WatchCommandSender @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun sendCommand(command: QuickCommand, roomId: String?) {
        val nodes = Wearable.getNodeClient(context).connectedNodes.await()
        if (nodes.isEmpty()) return

        val payload = CrewJson.encode(
            WatchCommand(
                commandId = UUID.randomUUID().toString(),
                roomId = roomId,
                command = command,
                senderDeviceName = Build.MODEL ?: "Watch",
            )
        ).toByteArray(Charsets.UTF_8)

        nodes.forEach { node ->
            runCatching {
                Wearable.getMessageClient(context)
                    .sendMessage(node.id, WearPaths.WATCH_COMMAND_PATH, payload)
                    .await()
            }
        }
    }
}
