package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlin.math.roundToInt
import androidx.compose.animation.core.tween

@Composable
fun CountingNumber(
    target: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    suffix: String = "",
    decimals: Int = 0,
    durationMs: Int = 1200
) {
    var start by remember { mutableStateOf(false) }
    val animatedValue by animateFloatAsState(
        targetValue = if (start) target.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "CountingNumber"
    )

    LaunchedEffect(Unit) { start = true }

    val displayed = if (decimals == 0) {
        animatedValue.roundToInt().toString()
    } else {
        "%.${decimals}f".format(animatedValue)
    }

    Text(text = "$displayed$suffix", modifier = modifier, style = style, color = color)
}
