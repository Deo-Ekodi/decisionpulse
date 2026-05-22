package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.decisionpulse.demo.ui.theme.Border2
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPGreenDim

@Composable
fun MonthlyBarsChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    barColor: Color = DPGreen,
    trackColor: Color = Border2,
    highlightLast: Boolean = true,
    durationMs: Int = 1200
) {
    if (data.isEmpty()) return

    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
        label = "BarsProgress"
    )
    LaunchedEffect(Unit) { started = true }

    val maxVal = data.max().coerceAtLeast(1.0)

    Canvas(modifier = modifier) {
        val count     = data.size
        val gapRatio  = 0.35f
        val totalGap  = size.width * gapRatio
        val barWidth  = (size.width - totalGap) / count
        val gapWidth  = if (count > 1) totalGap / (count - 1) else 0f
        val radius    = CornerRadius(barWidth * 0.3f, barWidth * 0.3f)

        data.forEachIndexed { i, v ->
            val x          = i * (barWidth + gapWidth)
            val fillHeight = (v / maxVal * size.height * 0.9f * progress).toFloat()
            val isLast     = i == count - 1
            val color      = if (highlightLast && isLast) barColor else barColor.copy(alpha = 0.55f)

            // Track
            drawRoundRect(
                color      = trackColor,
                topLeft    = Offset(x, 0f),
                size       = Size(barWidth, size.height),
                cornerRadius = radius
            )

            // Value bar (grows upward)
            if (fillHeight > 0f) {
                drawRoundRect(
                    color      = color,
                    topLeft    = Offset(x, size.height - fillHeight),
                    size       = Size(barWidth, fillHeight),
                    cornerRadius = radius
                )
            }
        }
    }
}