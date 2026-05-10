package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.NearbyCrew
import com.crewcomms.phone.ui.components.BrassCard
import com.crewcomms.phone.ui.components.CompassLoadingIndicator
import com.crewcomms.phone.ui.components.CrewButton
import com.crewcomms.phone.ui.components.PirateScaffold
import com.crewcomms.phone.ui.components.SignalLevel
import com.crewcomms.phone.ui.components.SignalStatusChip
import com.crewcomms.phone.ui.theme.BrassHighlight
import com.crewcomms.phone.ui.theme.CrewSpacing

@Composable
fun JoinCrewScreen(
    crews: List<NearbyCrew>,
    status: ConnectionStatus,
    onBack: () -> Unit,
    onScan: () -> Unit,
    onJoin: (NearbyCrew, String?) -> Unit,
) {
    val pinValues = remember { mutableStateMapOf<String, String>() }

    PirateScaffold(
        title = "Nearby Crews",
        subtitle = "Scan the horizon and board a captain's channel.",
    ) {
        SignalStatusChip(
            text = when (status) {
                ConnectionStatus.DISCOVERING -> "Scanning the horizon…"
                ConnectionStatus.CONNECTING -> "Hooking the line…"
                else -> "Signal standby"
            },
            level = when (status) {
                ConnectionStatus.CONNECTED -> SignalLevel.ONLINE
                ConnectionStatus.CONNECTING, ConnectionStatus.DISCOVERING -> SignalLevel.RECONNECTING
                ConnectionStatus.ERROR -> SignalLevel.LOST
                else -> SignalLevel.IDLE
            },
        )

        CrewButton(label = "Scan the Horizon", onClick = onScan, leadingIcon = Icons.Outlined.Explore)
        CrewButton(label = "Back", onClick = onBack, leadingIcon = Icons.Outlined.KeyboardReturn)

        if (status == ConnectionStatus.DISCOVERING) {
            CompassLoadingIndicator()
        }

        if (crews.isEmpty() && status != ConnectionStatus.DISCOVERING) {
            BrassCard(title = "Spyglass Sweep") {
                Text("No crews spotted nearby.", style = MaterialTheme.typography.titleMedium)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(CrewSpacing.sm)) {
            crews.forEach { crew ->
                BrassCard(title = "Crew Signal") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(crew.roomName, style = MaterialTheme.typography.titleLarge)
                        if (crew.pinRequired) {
                            Icon(Icons.Outlined.Lock, contentDescription = "PIN Protected", tint = BrassHighlight)
                        }
                    }

                    Text("Captain: ${crew.hostName}")
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
                        Icon(Icons.Outlined.RadioButtonChecked, contentDescription = null)
                        Text("Signal strength: ${crew.signalStrengthHint ?: "--"}")
                    }

                    if (crew.pinRequired) {
                        OutlinedTextField(
                            value = pinValues[crew.endpointId].orEmpty(),
                            onValueChange = { pinValues[crew.endpointId] = it.filter(Char::isDigit).take(6) },
                            label = { Text("PIN") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    CrewButton(label = "Join Crew", onClick = {
                        onJoin(crew, pinValues[crew.endpointId])
                    })
                }
            }
        }
    }
}
