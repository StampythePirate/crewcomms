package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.crewcomms.watch.ui.components.WatchCrewButton
import com.crewcomms.watch.ui.components.WatchCrewButtonStyle
import com.crewcomms.watch.ui.components.WatchStatusDot
import com.crewcomms.watch.ui.theme.CrewCommsWatchTheme
import com.crewcomms.watch.ui.theme.WatchAlertRed
import com.crewcomms.watch.ui.theme.WatchSignalGreen

@Composable
fun WatchHomeScreen(
    roomName: String?,
    status: String,
    onOpenControls: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenPtt: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("CrewComms", style = MaterialTheme.typography.title2)
        Text(roomName ?: "No active room", style = MaterialTheme.typography.caption1)

        WatchStatusDot(
            label = statusLabel(status),
            color = if (status == "CONNECTED") WatchSignalGreen else WatchAlertRed,
            modifier = Modifier.fillMaxWidth(),
        )

        WatchCrewButton(
            label = "OPEN CHANNEL",
            onClick = onOpenControls,
            style = WatchCrewButtonStyle.PRIMARY,
        )
        WatchCrewButton(label = "MESSAGES", onClick = onOpenMessages, style = WatchCrewButtonStyle.WAIT)
        WatchCrewButton(label = "VOICE", onClick = onOpenPtt, style = WatchCrewButtonStyle.WAIT)
    }
}

private fun statusLabel(status: String): String = when (status) {
    "CONNECTED" -> "ONLINE"
    "SYNCING", "CONNECTING", "DISCOVERING", "ADVERTISING" -> "NO SIGNAL"
    else -> "PHONE LOST"
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
private fun WatchHomePreview() {
    CrewCommsWatchTheme {
        WatchHomeScreen(
            roomName = "Captain's Channel",
            status = "CONNECTED",
            onOpenControls = {},
            onOpenMessages = {},
            onOpenPtt = {},
        )
    }
}
