package com.crewcomms.watch.service

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.common.VibrationConstants
import com.crewcomms.core.model.WatchState
import com.crewcomms.watch.data.WearPaths
import com.crewcomms.watch.data.WatchStateBus
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.WearableListenerService

class WatchWearListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type != DataEvent.TYPE_CHANGED) return@forEach
            val item = event.dataItem
            if (item.uri.path != WearPaths.WATCH_STATE_PATH) return@forEach

            val payload = com.google.android.gms.wearable.DataMapItem.fromDataItem(item)
                .dataMap
                .getString("payload")
                .orEmpty()

            val state = runCatching { CrewJson.decode<WatchState>(payload) }.getOrNull() ?: return@forEach
            val previous = WatchStateBus.state.value
            WatchStateBus.update(state)

            if (state.lastEventTimestamp > previous.lastEventTimestamp) {
                vibrateMessage()
            }
        }
    }

    private fun vibrateMessage() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as? Vibrator
        } ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(VibrationConstants.MESSAGE_PATTERN, -1),
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VibrationConstants.MESSAGE_PATTERN, -1)
        }
    }
}
