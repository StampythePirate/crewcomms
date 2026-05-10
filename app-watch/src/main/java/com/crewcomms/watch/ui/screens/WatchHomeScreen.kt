package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.compose.ui.tooling.preview.Preview
import com.crewcomms.watch.ui.theme.CrewCommsWatchTheme

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
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("CrewComms", style = MaterialTheme.typography.title2)
        Text("Phone: ${statusLabel(status)}", style = MaterialTheme.typography.caption1)
        Text("Room: ${roomName ?: "No active room"}", style = MaterialTheme.typography.caption1)

        Button(onClick = onOpenControls, modifier = Modifier.fillMaxWidth()) { Text("Open Crew Controls") }
        Button(onClick = onOpenMessages, modifier = Modifier.fillMaxWidth()) { Text("Messages") }
        Button(onClick = onOpenPtt, modifier = Modifier.fillMaxWidth()) { Text("Push To Talk") }
    }
}

private fun statusLabel(status: String): String = when (status) {
    "CONNECTED" -> "Connected to phone"
    "SYNCING", "CONNECTING", "DISCOVERING", "ADVERTISING" -> "Syncing"
    else -> "Disconnected"
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
private fun WatchHomePreview() {
    CrewCommsWatchTheme {
        WatchHomeScreen(
            roomName = "Alpha Team",
            status = "CONNECTED",
            onOpenControls = {},
            onOpenMessages = {},
            onOpenPtt = {},
        )
    }
}
