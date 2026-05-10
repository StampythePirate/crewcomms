package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.phone.ui.PhoneUiState
import com.crewcomms.phone.ui.components.BrassCard
import com.crewcomms.phone.ui.components.ConfirmDangerDialog
import com.crewcomms.phone.ui.components.CrewButton
import com.crewcomms.phone.ui.components.CrewMessageBubble
import com.crewcomms.phone.ui.components.DangerCrewButton
import com.crewcomms.phone.ui.components.MemberStatusRow
import com.crewcomms.phone.ui.components.PirateScaffold
import com.crewcomms.phone.ui.components.SignalLevel
import com.crewcomms.phone.ui.theme.CrewSpacing

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
    var confirmEmergency by remember { mutableStateOf(false) }

    val signalLevel = when (state.status) {
        ConnectionStatus.CONNECTED -> SignalLevel.ONLINE
        ConnectionStatus.CONNECTING, ConnectionStatus.SYNCING, ConnectionStatus.DISCOVERING, ConnectionStatus.ADVERTISING -> SignalLevel.RECONNECTING
        ConnectionStatus.ERROR, ConnectionStatus.DISCONNECTED -> SignalLevel.LOST
    }

    val signalText = when (state.status) {
        ConnectionStatus.CONNECTED -> "Signal Online"
        ConnectionStatus.CONNECTING, ConnectionStatus.SYNCING, ConnectionStatus.DISCOVERING, ConnectionStatus.ADVERTISING -> "Reconnecting"
        ConnectionStatus.ERROR, ConnectionStatus.DISCONNECTED -> "Signal Lost"
    }

    PirateScaffold(
        title = "Captain's Channel: ${state.room?.name ?: "No Channel"}",
        subtitle = "Close-range crew communication",
    ) {
        MemberStatusRow(
            memberCount = state.members.size,
            signalText = signalText,
            level = signalLevel,
        )

        BrassCard(title = "Captain's Log") {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(280.dp),
                verticalArrangement = Arrangement.spacedBy(CrewSpacing.xs),
            ) {
                items(state.messages, key = { it.id }) { message ->
                    CrewMessageBubble(
                        message = message,
                        isOwn = message.senderId == "self",
                    )
                }
            }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Send message") },
                modifier = Modifier.fillMaxWidth(),
            )

            CrewButton(
                label = "Send",
                onClick = {
                    onSendMessage(text)
                    text = ""
                },
            )
        }

        BrassCard(title = "Quick Commands") {
            CrewButton(label = "Signal Ping", onClick = { onQuickCommand(QuickCommand.PING) })
            DangerCrewButton(label = "Emergency Flare") { confirmEmergency = true }
            CrewButton(label = "Ready on Deck", onClick = { onQuickCommand(QuickCommand.READY) })
            CrewButton(label = "Rally Here", onClick = { onQuickCommand(QuickCommand.COME_HERE) })
            CrewButton(label = "Stand By", onClick = { onQuickCommand(QuickCommand.WAIT) })
            CrewButton(label = "Hold to Speak", onClick = onVoicePlaceholder)
        }

        CrewButton(label = "Back", onClick = onBack)
        DangerCrewButton(label = "Leave Channel", onClick = onLeaveCrew)
    }

    ConfirmDangerDialog(
        visible = confirmEmergency,
        title = "Send Emergency Flare?",
        text = "This will alert the entire nearby crew channel.",
        confirmLabel = "Send Flare",
        onConfirm = {
            confirmEmergency = false
            onQuickCommand(QuickCommand.HELP)
        },
        onDismiss = { confirmEmergency = false },
    )
}
