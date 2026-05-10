package com.crewcomms.phone.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BlackTideColors = darkColorScheme(
    primary = BrassGold,
    onPrimary = TideBlack,
    secondary = BrassHighlight,
    tertiary = SignalGreen,
    background = TideBlack,
    onBackground = AgedParchment,
    surface = OceanPanel,
    onSurface = AgedParchment,
    error = AlertRed,
    onError = AgedParchment,
)

@Composable
fun CrewCommsPhoneTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BlackTideColors,
        typography = Typography,
        content = content,
    )
}
