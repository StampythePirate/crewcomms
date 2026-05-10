package com.crewcomms.watch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.crewcomms.core.common.TimeFormatter
import com.crewcomms.core.model.WatchMessagePreview

@Composable
fun WatchMessagesScreen(messages: List<WatchMessagePreview>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("Last Signals", style = MaterialTheme.typography.title3)

        messages.take(5).forEach { item ->
            Column(modifier = Modifier.padding(6.dp)) {
                Text("${item.senderName} ${TimeFormatter.formatTime(item.timestamp)}")
                Text(item.body)
            }
        }

        if (messages.isEmpty()) {
            Text("No messages")
        }
    }
}
