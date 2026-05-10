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
fun WatchPttPlaceholderScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Push To Talk", style = MaterialTheme.typography.title2)
        Text("Hold to Talk UI wired for v1 placeholder")

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("HOLD TO TALK")
        }

        Text("TODO: implement Opus encoding + real-time audio streaming in v2")
    }
}
