package com.decisionpulse.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary          = DPGreen,
    onPrimary        = BgDeep,
    primaryContainer = DPGreenDim,
    secondary        = DPBlue,
    onSecondary      = BgDeep,
    background       = BgDeep,
    surface          = BgSurface,
    surfaceVariant   = BgSurface2,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    error            = DPRed,
    outline          = Border2
)

@Composable
fun DecisionPulseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}