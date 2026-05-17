package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.theme.*
import androidx.compose.animation.core.tween

@Composable
fun TrajectoryChart(
    readings: List<Double>,     // ordered chronologically
    target: Double = 100.0,
    modifier: Modifier = Modifier,
    lineColor: Color = DPGreen,
    strokeWidth: Dp = 2.5.dp,
    showGradient: Boolean = true,
    durationMs: Int = 1800
) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "ChartDraw"
    )

    LaunchedEffect(Unit) { started = true }

    if (readings.isEmpty()) return

    Canvas(modifier = modifier) {
        val maxVal = maxOf(readings.max(), target) * 1.08
        val minVal = readings.min() * 0.88
        val range = maxVal - minVal

        val w = size.width
        val h = size.height

        fun xOf(index: Int) = w * index / (readings.size - 1)
        fun yOf(v: Double) = h - ((v - minVal) / range * h).toFloat()

        // Target dashed line
        val targetY = yOf(target)
        val dashWidth = 12f; val dashGap = 8f
        var x = 0f
        while (x < w) {
            drawLine(
                color = DPAmber.copy(alpha = 0.45f),
                start = Offset(x, targetY),
                end = Offset(minOf(x + dashWidth, w), targetY),
                strokeWidth = 1.5.dp.toPx()
            )
            x += dashWidth + dashGap
        }

        // Build full path
        val fullPath = Path().apply {
            readings.forEachIndexed { i, v ->
                val px = xOf(i); val py = yOf(v)
                if (i == 0) moveTo(px, py) else lineTo(px, py)
            }
        }

        // Clip to animated progress (left → right reveal)
        val pm = PathMeasure()
        pm.setPath(fullPath, false)
        val totalLength = pm.length
        val clippedPath = Path()
        pm.getSegment(0f, totalLength * progress, clippedPath, true)

        // Gradient fill under line
        if (showGradient) {
            val fillPath = Path().apply {
                addPath(clippedPath)
                // close down to bottom
                val lastIdx = (readings.size * progress).toInt().coerceAtMost(readings.size - 1)
                lineTo(xOf(lastIdx), h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0f))
                )
            )
        }

        // Chart line
        drawPath(
            path = clippedPath,
            color = lineColor,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
