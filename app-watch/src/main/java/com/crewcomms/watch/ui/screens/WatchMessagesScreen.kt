package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.crewcomms.core.common.TimeFormatter
import com.crewcomms.core.model.WatchMessagePreview
import com.crewcomms.watch.ui.theme.WatchBrassHighlight
import com.crewcomms.watch.ui.theme.WatchOceanPanel

@Composable
fun WatchMessagesScreen(messages: List<WatchMessagePreview>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text("LAST 3 SIGNALS", style = MaterialTheme.typography.caption1)

        messages.take(3).forEach { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WatchOceanPanel, RoundedCornerShape(10.dp))
                    .border(1.dp, WatchBrassHighlight.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
                    .padding(6.dp),
            ) {
                Text("${item.senderName} ${TimeFormatter.formatTime(item.timestamp)}", style = MaterialTheme.typography.caption3)
                Text(item.body, style = MaterialTheme.typography.caption2)
            }
        }

        if (messages.isEmpty()) {
            Text("No signal logs")
        }
    }
}
