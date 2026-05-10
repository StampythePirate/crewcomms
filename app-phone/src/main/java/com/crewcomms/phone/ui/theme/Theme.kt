package com.crewcomms.phone.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CrewDarkScheme = darkColorScheme(
    primary = CrewGreen,
    secondary = SignalCyan,
    tertiary = AlertGold,
    background = DeepNavy,
    surface = CarbonBlack,
)

@Composable
fun CrewCommsPhoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) CrewDarkScheme else CrewDarkScheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
