package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.crewcomms.watch.ui.theme.WatchBrassGold
import com.crewcomms.watch.ui.theme.WatchParchment
import com.crewcomms.watch.ui.theme.WatchSignalGreen
import com.crewcomms.watch.ui.theme.WatchTideBlack

@Composable
fun WatchPttPlaceholderScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("HOLD TO SPEAK", style = MaterialTheme.typography.title3)

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (pressed) WatchSignalGreen else WatchBrassGold,
                contentColor = WatchTideBlack,
            ),
        ) {
            Text(if (pressed) "TRANSMITTING" else "HOLD TO SPEAK")
        }

        Text("VOICE COMING SOON", color = WatchParchment)
    }
}
