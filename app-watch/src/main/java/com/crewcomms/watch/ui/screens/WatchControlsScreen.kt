package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.crewcomms.watch.ui.components.WatchCrewButton
import com.crewcomms.watch.ui.components.WatchCrewButtonStyle
import com.crewcomms.watch.ui.components.WatchStatusDot
import com.crewcomms.watch.ui.theme.WatchAlertRed
import com.crewcomms.watch.ui.theme.WatchSignalGreen

@Composable
fun WatchControlsScreen(
    roomName: String?,
    status: String,
    lastMessage: String?,
    onPing: () -> Unit,
    onHelp: () -> Unit,
    onReady: () -> Unit,
    onCome: () -> Unit,
    onWait: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(roomName ?: "No Channel", style = MaterialTheme.typography.title3)
        WatchStatusDot(
            label = if (status == "CONNECTED") "ONLINE" else "NO SIGNAL",
            color = if (status == "CONNECTED") WatchSignalGreen else WatchAlertRed,
        )
        Text(lastMessage ?: "No recent signal", style = MaterialTheme.typography.caption2)

        WatchCrewButton("PING", onPing, WatchCrewButtonStyle.PING)
        WatchCrewButton("HELP", onHelp, WatchCrewButtonStyle.HELP)
        WatchCrewButton("READY", onReady, WatchCrewButtonStyle.READY)
        WatchCrewButton("RALLY", onCome, WatchCrewButtonStyle.RALLY)
        WatchCrewButton("WAIT", onWait, WatchCrewButtonStyle.WAIT)
    }
}
