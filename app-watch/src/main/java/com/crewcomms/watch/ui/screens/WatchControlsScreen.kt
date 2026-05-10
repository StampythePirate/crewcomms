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
        Text(roomName ?: "No Crew", style = MaterialTheme.typography.title3)
        Text("Signal: ${statusLabel(status)}", style = MaterialTheme.typography.caption2)
        Text(lastMessage ?: "No recent signal", style = MaterialTheme.typography.caption2)

        CommandButton("PING", onPing)
        CommandButton("HELP", onHelp)
        CommandButton("READY", onReady)
        CommandButton("COME", onCome)
        CommandButton("WAIT", onWait)
    }
}

@Composable
private fun CommandButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(label)
    }
}

private fun statusLabel(status: String): String = when (status) {
    "CONNECTED" -> "Online"
    "SYNCING", "CONNECTING", "DISCOVERING", "ADVERTISING" -> "Syncing"
    else -> "No Signal"
}
