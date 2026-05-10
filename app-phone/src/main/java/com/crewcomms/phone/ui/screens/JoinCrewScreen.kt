package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.NearbyCrew

@Composable
fun JoinCrewScreen(
    crews: List<NearbyCrew>,
    status: ConnectionStatus,
    onBack: () -> Unit,
    onScan: () -> Unit,
    onJoin: (NearbyCrew, String?) -> Unit,
) {
    val pinValues = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Nearby Crews", style = MaterialTheme.typography.headlineSmall)
        Text("Scan status: ${status.name}")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onScan) { Text("Start Scan") }
            Button(onClick = onBack) { Text("Back") }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(crews, key = { it.endpointId }) { crew ->
                Card {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(crew.roomName, style = MaterialTheme.typography.titleMedium)
                        Text("Host: ${crew.hostName}")
                        Text("Signal: ${crew.signalStrengthHint?.toString() ?: "--"}")

                        if (crew.pinRequired) {
                            OutlinedTextField(
                                value = pinValues[crew.endpointId].orEmpty(),
                                onValueChange = { pinValues[crew.endpointId] = it.filter(Char::isDigit).take(6) },
                                label = { Text("PIN") },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        Button(onClick = { onJoin(crew, pinValues[crew.endpointId]) }) {
                            Text("Join Crew")
                        }
                    }
                }
            }
        }
    }
}
