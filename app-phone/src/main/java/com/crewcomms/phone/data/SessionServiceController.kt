package com.crewcomms.phone.data

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.crewcomms.phone.service.CrewSessionForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionServiceController @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun start() {
        val intent = Intent(context, CrewSessionForegroundService::class.java)
        runCatching {
            ContextCompat.startForegroundService(context, intent)
        }
    }

    fun stop() {
        context.stopService(Intent(context, CrewSessionForegroundService::class.java))
    }
}
