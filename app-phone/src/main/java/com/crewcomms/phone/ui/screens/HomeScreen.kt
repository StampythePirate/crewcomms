package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crewcomms.phone.ui.theme.CrewCommsPhoneTheme

@Composable
fun HomeScreen(
    deviceName: String,
    onCreateCrew: () -> Unit,
    onJoinCrew: () -> Unit,
    onOpenLastCrew: () -> Unit,
    onSettings: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("CrewComms", style = MaterialTheme.typography.displaySmall)
        Text("Captain: $deviceName", style = MaterialTheme.typography.titleMedium)

        ActionButton(label = "Create Crew", onClick = onCreateCrew)
        ActionButton(label = "Join Nearby Crew", onClick = onJoinCrew)
        ActionButton(label = "Open Last Crew", onClick = onOpenLastCrew)
        ActionButton(label = "Settings", onClick = onSettings)
    }
}

@Composable
private fun ActionButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(label)
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    CrewCommsPhoneTheme {
        HomeScreen(
            deviceName = "Captain Vega",
            onCreateCrew = {},
            onJoinCrew = {},
            onOpenLastCrew = {},
            onSettings = {},
        )
    }
}
