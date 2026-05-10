package com.crewcomms.watch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.crewcomms.watch.ui.theme.WatchAlertRed
import com.crewcomms.watch.ui.theme.WatchBrassGold
import com.crewcomms.watch.ui.theme.WatchBrassHighlight
import com.crewcomms.watch.ui.theme.WatchMutedText
import com.crewcomms.watch.ui.theme.WatchParchment
import com.crewcomms.watch.ui.theme.WatchSignalGreen
import com.crewcomms.watch.ui.theme.WatchTideBlack

enum class WatchCrewButtonStyle {
    PRIMARY,
    PING,
    HELP,
    READY,
    RALLY,
    WAIT,
}

@Composable
fun WatchCrewButton(
    label: String,
    onClick: () -> Unit,
    style: WatchCrewButtonStyle,
    modifier: Modifier = Modifier,
) {
    val background = when (style) {
        WatchCrewButtonStyle.PRIMARY -> WatchBrassGold
        WatchCrewButtonStyle.PING -> WatchBrassHighlight
        WatchCrewButtonStyle.HELP -> WatchAlertRed
        WatchCrewButtonStyle.READY -> WatchSignalGreen
        WatchCrewButtonStyle.RALLY -> WatchBrassGold
        WatchCrewButtonStyle.WAIT -> WatchMutedText
    }

    val contentColor = if (style == WatchCrewButtonStyle.HELP) WatchParchment else WatchTideBlack

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
            contentColor = contentColor,
        ),
    ) {
        Text(label)
    }
}

@Composable
fun WatchStatusDot(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(999.dp))
            .border(1.dp, color, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .background(color, RoundedCornerShape(99.dp))
                .padding(4.dp),
        )
        Text(label)
    }
}
