package com.crewcomms.watch.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun CrewCommsWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = WatchBrassGold,
            secondary = WatchBrassHighlight,
            background = WatchTideBlack,
            surface = WatchOceanPanel,
            error = WatchAlertRed,
            onPrimary = WatchTideBlack,
            onSecondary = WatchParchment,
            onBackground = WatchParchment,
            onSurface = WatchParchment,
            onError = WatchParchment,
        ),
        content = content,
    )
}
