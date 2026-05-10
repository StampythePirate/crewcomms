package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crewcomms.phone.ui.components.BrassCard
import com.crewcomms.phone.ui.components.CrewButton
import com.crewcomms.phone.ui.components.PirateScaffold
import com.crewcomms.phone.ui.components.SignalLevel
import com.crewcomms.phone.ui.components.SignalStatusChip
import com.crewcomms.phone.ui.theme.CrewCommsPhoneTheme
import com.crewcomms.phone.ui.theme.CrewSpacing

@Composable
fun HomeScreen(
    deviceName: String,
    watchLinked: Boolean,
    signalLevel: SignalLevel,
    signalText: String,
    onCreateCrew: () -> Unit,
    onJoinCrew: () -> Unit,
    onOpenLastCrew: () -> Unit,
    onSettings: () -> Unit,
) {
    PirateScaffold(
        title = "CrewComms",
        subtitle = "Black Tide Command | Private nearby crew communication",
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
            Row(horizontalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
                SignalStatusChip(text = signalText, level = signalLevel)
                SignalStatusChip(text = "Phone Ready", level = SignalLevel.ONLINE)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
                SignalStatusChip(
                    text = if (watchLinked) "Watch Linked" else "Watch Not Linked",
                    level = if (watchLinked) SignalLevel.ONLINE else SignalLevel.IDLE,
                )
            }
        }

        BrassCard(title = "Captain Command Panel") {
            CrewButton(label = "Create Crew", onClick = onCreateCrew, leadingIcon = Icons.Outlined.Hub)
            CrewButton(label = "Join Nearby Crew", onClick = onJoinCrew, leadingIcon = Icons.Outlined.Map)
            CrewButton(label = "Open Last Channel", onClick = onOpenLastCrew, leadingIcon = Icons.Outlined.Groups)
            CrewButton(label = "Settings", onClick = onSettings, leadingIcon = Icons.Outlined.Settings)
        }

        Column(verticalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
            SignalStatusChip(text = "Captain: $deviceName", level = SignalLevel.IDLE)
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    CrewCommsPhoneTheme {
        HomeScreen(
            deviceName = "Captain Gray",
            watchLinked = true,
            signalLevel = SignalLevel.IDLE,
            signalText = "Signal Idle",
            onCreateCrew = {},
            onJoinCrew = {},
            onOpenLastCrew = {},
            onSettings = {},
        )
    }
}
