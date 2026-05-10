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
import com.crewcomms.core.model.CrewMember

@Composable
fun CreateCrewScreen(
    members: List<CrewMember>,
    onBack: () -> Unit,
    onStartCrew: (name: String, pinRequired: Boolean, pin: String?) -> Unit,
) {
    var crewName by remember { mutableStateOf("") }
    var pinRequired by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Create Crew", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = crewName,
            onValueChange = { crewName = it },
            label = { Text("Crew name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("PIN protected")
            Switch(checked = pinRequired, onCheckedChange = { pinRequired = it })
        }

        if (pinRequired) {
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it.filter { c -> c.isDigit() }.take(6) },
                label = { Text("Room PIN") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Button(onClick = { onStartCrew(crewName, pinRequired, pin.ifBlank { null }) }, modifier = Modifier.fillMaxWidth()) {
            Text("Start Advertising")
        }

        Text("Nearby joined members", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(members) { member -> Text("- ${member.displayName}") }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
