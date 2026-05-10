package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.crewcomms.phone.data.PhoneSettings
import com.crewcomms.phone.ui.components.BrassCard
import com.crewcomms.phone.ui.components.CrewButton
import com.crewcomms.phone.ui.components.DangerCrewButton
import com.crewcomms.phone.ui.components.PirateScaffold

@Composable
fun SettingsScreen(
    settings: PhoneSettings,
    onBack: () -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onVibrationRelayChanged: (Boolean) -> Unit,
    onKeepAwakeChanged: (Boolean) -> Unit,
    onAutoReconnectChanged: (Boolean) -> Unit,
    onMockModeChanged: (Boolean) -> Unit,
    onClearHistory: () -> Unit,
) {
    var displayName by remember(settings.displayName) { mutableStateOf(settings.displayName) }

    PirateScaffold(
        title = "Settings",
        subtitle = "Captain device controls",
    ) {
        BrassCard(title = "Captain Controls") {
            OutlinedTextField(
                value = displayName,
                onValueChange = {
                    displayName = it
                    onDisplayNameChanged(it)
                },
                label = { Text("Captain Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            ToggleRow("Vibration Alerts", settings.vibrationRelayToWatch, onVibrationRelayChanged)
            ToggleRow("Auto-Reconnect", settings.autoReconnect, onAutoReconnectChanged)
            ToggleRow("Mock Transport Mode", settings.useMockTransport, onMockModeChanged)
            ToggleRow("Keep Channel Active", settings.keepScreenAwake, onKeepAwakeChanged)
        }

        DangerCrewButton(label = "Clear Logs", onClick = onClearHistory)
        CrewButton(label = "Back", onClick = onBack)
    }
}

@Composable
private fun ToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
