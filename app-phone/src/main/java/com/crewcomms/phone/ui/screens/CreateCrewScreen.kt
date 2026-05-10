package com.crewcomms.phone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.crewcomms.core.model.CrewMember
import com.crewcomms.phone.ui.components.BrassCard
import com.crewcomms.phone.ui.components.CrewButton
import com.crewcomms.phone.ui.components.ParchmentCodeCard
import com.crewcomms.phone.ui.components.PirateScaffold
import com.crewcomms.phone.ui.theme.CrewSpacing

@Composable
fun CreateCrewScreen(
    members: List<CrewMember>,
    onBack: () -> Unit,
    onStartCrew: (name: String, pinRequired: Boolean, pin: String?) -> Unit,
) {
    var crewName by remember { mutableStateOf("") }
    var pinProtectedCove by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }

    PirateScaffold(
        title = "Name Your Crew",
        subtitle = "Set channel visibility before you raise the signal.",
    ) {
        BrassCard(title = "Channel Setup") {
            OutlinedTextField(
                value = crewName,
                onValueChange = { crewName = it },
                label = { Text("Crew name") },
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Channel Visibility", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(if (pinProtectedCove) "PIN-Protected Cove" else "Open Waters")
                Switch(checked = pinProtectedCove, onCheckedChange = { pinProtectedCove = it })
            }

            if (pinProtectedCove) {
                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it.filter(Char::isDigit).take(6) },
                    label = { Text("Cove PIN") },
                    modifier = Modifier.fillMaxWidth(),
                )
                ParchmentCodeCard(code = pin)
            }

            CrewButton(label = "Raise the Signal", onClick = {
                onStartCrew(crewName, pinProtectedCove, pin.ifBlank { null })
            }, shimmer = true)
            CrewButton(label = "Back", onClick = onBack)
        }

        BrassCard(title = "Crew Members") {
            if (members.isEmpty()) {
                Text("No crew joined yet.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(CrewSpacing.xs)) {
                    items(members) { member ->
                        Text("• ${member.displayName}")
                    }
                }
            }
        }
    }
}
