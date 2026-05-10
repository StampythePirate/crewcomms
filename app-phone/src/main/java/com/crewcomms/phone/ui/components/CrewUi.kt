package com.crewcomms.phone.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.MessageType
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.phone.ui.theme.AgedParchment
import com.crewcomms.phone.ui.theme.AlertRed
import com.crewcomms.phone.ui.theme.BrassGold
import com.crewcomms.phone.ui.theme.BrassHighlight
import com.crewcomms.phone.ui.theme.CrewSpacing
import com.crewcomms.phone.ui.theme.MidnightNavy
import com.crewcomms.phone.ui.theme.MutedText
import com.crewcomms.phone.ui.theme.OceanPanel
import com.crewcomms.phone.ui.theme.SignalGreen
import com.crewcomms.phone.ui.theme.TideBlack

enum class SignalLevel {
    ONLINE,
    LOST,
    RECONNECTING,
    IDLE,
}

@Composable
fun PirateScaffold(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(TideBlack, MidnightNavy, OceanPanel))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(CrewSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(CrewSpacing.md),
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.displaySmall,
                color = AgedParchment,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(Icons.Outlined.Navigation, contentDescription = null, tint = BrassHighlight)
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = BrassHighlight.copy(alpha = 0.4f))
            }
            content()
        }
    }
}

@Composable
fun BrassCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(OceanPanel.copy(alpha = 0.96f), TideBlack.copy(alpha = 0.88f)),
                ),
                RoundedCornerShape(16.dp),
            )
            .border(1.dp, BrassHighlight.copy(alpha = 0.72f), RoundedCornerShape(16.dp))
            .padding(CrewSpacing.md),
        verticalArrangement = Arrangement.spacedBy(CrewSpacing.sm),
    ) {
        if (!title.isNullOrBlank()) {
            Text(title.uppercase(), color = BrassHighlight, style = MaterialTheme.typography.labelLarge)
            HorizontalDivider(color = BrassHighlight.copy(alpha = 0.35f))
        }
        content()
    }
}

@Composable
fun SignalStatusChip(
    text: String,
    level: SignalLevel,
    modifier: Modifier = Modifier,
) {
    val color = when (level) {
        SignalLevel.ONLINE -> SignalGreen
        SignalLevel.LOST -> AlertRed
        SignalLevel.RECONNECTING -> BrassHighlight
        SignalLevel.IDLE -> MutedText
    }

    val pulse = rememberInfiniteTransition(label = "signal-pulse").animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "signal-alpha",
    )

    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.13f), RoundedCornerShape(999.dp))
            .border(1.dp, color.copy(alpha = 0.7f), RoundedCornerShape(999.dp))
            .padding(horizontal = 11.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .alpha(pulse.value)
                .background(color, RoundedCornerShape(99.dp))
                .padding(5.dp),
        )
        Text(text = text, color = color, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun CrewButton(
    label: String,
    onClick: () -> Unit,
    shimmer: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    val glow = if (shimmer) {
        rememberInfiniteTransition(label = "raise-signal").animateFloat(
            initialValue = 0.88f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "raise-signal-alpha",
        ).value
    } else {
        1f
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().alpha(glow),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrassGold,
            contentColor = TideBlack,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrassHighlight.copy(alpha = 0.8f)),
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null)
        }
        Text(label.uppercase(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DangerCrewButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AlertRed,
            contentColor = AgedParchment,
        ),
    ) {
        Text(label, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ParchmentCodeCard(
    code: String,
    label: String = "PIN-Protected Cove",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AgedParchment.copy(alpha = 0.93f), RoundedCornerShape(12.dp))
            .border(1.dp, BrassGold, RoundedCornerShape(12.dp))
            .padding(CrewSpacing.md),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(label, color = TideBlack, style = MaterialTheme.typography.labelLarge)
        Text(code.ifBlank { "••••••" }, color = TideBlack, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun CrewMessageBubble(
    message: CrewMessage,
    isOwn: Boolean,
) {
    when {
        message.type == MessageType.SYSTEM -> {
            Text(
                text = message.body,
                modifier = Modifier.fillMaxWidth(),
                color = BrassHighlight,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        message.quickCommand == QuickCommand.HELP -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AlertRed.copy(alpha = 0.26f), RoundedCornerShape(12.dp))
                    .border(1.dp, AlertRed, RoundedCornerShape(12.dp))
                    .padding(10.dp),
            ) {
                Text("EMERGENCY FLARE", color = AlertRed, fontWeight = FontWeight.Bold)
                Text("${message.senderName}: ${message.body}", color = AgedParchment)
            }
        }

        isOwn -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightNavy.copy(alpha = 0.88f), RoundedCornerShape(12.dp))
                    .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
            ) {
                Text(message.senderName, color = BrassHighlight, style = MaterialTheme.typography.labelLarge)
                Text(message.body, color = AgedParchment)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TideBlack.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
            ) {
                Text(message.senderName, color = MutedText, style = MaterialTheme.typography.labelLarge)
                Text(message.body, color = AgedParchment)
            }
        }
    }
}

@Composable
fun MemberStatusRow(
    memberCount: Int,
    signalText: String,
    level: SignalLevel,
) {
    BrassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.Groups, contentDescription = null, tint = BrassHighlight)
                Text("Crew: $memberCount", color = AgedParchment, style = MaterialTheme.typography.titleMedium)
            }
            SignalStatusChip(text = signalText, level = level)
        }
    }
}

@Composable
fun CompassLoadingIndicator(text: String = "Scanning the horizon…") {
    val sweep by rememberInfiniteTransition(label = "compass-sweep").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(1400)),
        label = "compass-rotation",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TideBlack.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .border(1.dp, BrassHighlight.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Explore,
            contentDescription = null,
            tint = BrassHighlight,
            modifier = Modifier.rotate(sweep),
        )
        Text(text, color = AgedParchment)
    }
}

@Composable
fun ConfirmDangerDialog(
    visible: Boolean,
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmLabel, color = AlertRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
