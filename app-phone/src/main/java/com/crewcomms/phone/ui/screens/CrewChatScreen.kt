package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crewcomms.core.common.TimeFormatter
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.phone.ui.PhoneUiState

@Composable
fun CrewChatScreen(
    state: PhoneUiState,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onQuickCommand: (QuickCommand) -> Unit,
    onVoicePlaceholder: () -> Unit,
    onLeaveCrew: () -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Crew Channel", style = MaterialTheme.typography.headlineSmall)
        Text("Room: ${state.room?.name ?: "No active room"}")
        Text("Signal: ${statusLabel(state.status.name)}")
        Text("Crew Members: ${state.members.joinToString { it.displayName }}")

        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(260.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(state.messages, key = { it.id }) { message ->
                Text("${TimeFormatter.formatTime(message.timestamp)} ${message.senderName}: ${message.body}")
            }
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                onSendMessage(text)
                text = ""
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Send") }

        Button(onClick = onVoicePlaceholder, modifier = Modifier.fillMaxWidth()) {
            Text("Send test voice note")
        }
        Button(onClick = onVoicePlaceholder, modifier = Modifier.fillMaxWidth()) {
            Text("Push-to-talk")
        }
        QuickAction("Ping") { onQuickCommand(QuickCommand.PING) }
        QuickAction("Help") { onQuickCommand(QuickCommand.HELP) }
        QuickAction("Ready") { onQuickCommand(QuickCommand.READY) }
        QuickAction("Come Here") { onQuickCommand(QuickCommand.COME_HERE) }
        QuickAction("Wait") { onQuickCommand(QuickCommand.WAIT) }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
        Button(onClick = onLeaveCrew, modifier = Modifier.fillMaxWidth()) { Text("Stop Session") }
    }
}

@Composable
private fun QuickAction(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(label)
    }
}

private fun statusLabel(status: String): String = when (status) {
    "CONNECTED" -> "Signal Online"
    "DISCOVERING", "ADVERTISING", "CONNECTING", "SYNCING" -> "Syncing Signal"
    else -> "No Signal"
}
