package com.crewcomms.phone.service

import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.model.WatchCommand
import com.crewcomms.phone.data.WatchCommandBus
import com.crewcomms.phone.data.WearPaths
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class PhoneWearListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != WearPaths.WATCH_COMMAND_PATH) return

        val payload = messageEvent.data?.toString(Charsets.UTF_8).orEmpty()
        runCatching {
            CrewJson.decode<WatchCommand>(payload)
        }.onSuccess {
            WatchCommandBus.tryEmit(it)
        }
    }
}
