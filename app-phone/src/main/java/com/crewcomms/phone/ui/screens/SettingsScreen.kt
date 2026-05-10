package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.crewcomms.phone.data.PhoneSettings

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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = displayName,
            onValueChange = {
                displayName = it
                onDisplayNameChanged(it)
            },
            label = { Text("Display name") },
            modifier = Modifier.fillMaxWidth(),
        )

        ToggleRow("Enable vibration relay to watch", settings.vibrationRelayToWatch, onVibrationRelayChanged)
        ToggleRow("Keep screen awake during active crew", settings.keepScreenAwake, onKeepAwakeChanged)
        ToggleRow("Auto-reconnect", settings.autoReconnect, onAutoReconnectChanged)
        ToggleRow("Use mock transport mode", settings.useMockTransport, onMockModeChanged)

        Button(onClick = onClearHistory, modifier = Modifier.fillMaxWidth()) {
            Text("Clear message history")
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
private fun ToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
