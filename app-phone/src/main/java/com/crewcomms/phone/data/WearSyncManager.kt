package com.crewcomms.phone.data

import com.crewcomms.core.common.CrewJson
import com.crewcomms.core.model.WatchState
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.content.Context
import kotlinx.coroutines.tasks.await

@Singleton
class WearSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun publishWatchState(state: WatchState) {
        scope.launch {
            val client = Wearable.getDataClient(context)
            val request = PutDataMapRequest.create(WearPaths.WATCH_STATE_PATH).apply {
                dataMap.putString("payload", CrewJson.encode(state))
                dataMap.putLong("ts", System.currentTimeMillis())
            }.asPutDataRequest().setUrgent()

            runCatching {
                client.putDataItem(request).await()
            }
        }
    }
}
