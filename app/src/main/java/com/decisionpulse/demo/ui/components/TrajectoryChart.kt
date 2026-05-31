package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPGreen

@Composable
fun TrajectoryChart(
    readings: List<Double>,
    target: Double = 100.0,
    modifier: Modifier = Modifier,
    lineColor: Color = DPGreen,
    strokeWidth: Dp = 2.5.dp,
    showGradient: Boolean = true,
    durationMs: Int = 1800
) {
    if (readings.isEmpty()) return

    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue   = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
        label         = "ChartDraw"
    )
    LaunchedEffect(Unit) { started = true }

    Canvas(modifier = modifier) {
        val maxVal = maxOf(readings.max(), target) * 1.08
        val minVal = readings.min() * 0.88
        val range  = (maxVal - minVal).coerceAtLeast(1.0)
        val w = size.width
        val h = size.height

        fun xOf(i: Int)    = w * i / (readings.size - 1).coerceAtLeast(1)
        fun yOf(v: Double) = h - ((v - minVal) / range * h).toFloat()

        val targetY = yOf(target)
        val dashW = 10f; val dashGap = 7f; var dx = 0f
        while (dx < w) {
            drawLine(
                color       = DPAmber.copy(alpha = 0.4f),
                start       = Offset(dx, targetY),
                end         = Offset((dx + dashW).coerceAtMost(w), targetY),
                strokeWidth = 1.5.dp.toPx()
            )
            dx += dashW + dashGap
        }

        val fullPath = Path().apply {
            readings.forEachIndexed { i, v ->
                val x = xOf(i); val y = yOf(v)
                if (i == 0) moveTo(x, y)
                else {
                    val prevX = xOf(i - 1); val prevY = yOf(readings[i - 1])
                    val cpX = (prevX + x) / 2f
                    cubicTo(cpX, prevY, cpX, y, x, y)
                }
            }
        }

        val pm = PathMeasure()
        pm.setPath(fullPath, false)
        val clipped = Path()
        pm.getSegment(0f, pm.length * progress, clipped, true)

        if (showGradient) {
            val lastIdx  = ((readings.size - 1) * progress).toInt().coerceIn(0, readings.size - 1)
            val fillPath = Path().apply {
                addPath(clipped)
                lineTo(xOf(lastIdx), h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path  = fillPath,
                brush = Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.22f), lineColor.copy(alpha = 0f)))
            )
        }

        drawPath(clipped, lineColor, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

        if (progress > 0.98f && readings.isNotEmpty()) {
            val li = readings.size - 1
            drawCircle(lineColor, strokeWidth.toPx() * 2f, Offset(xOf(li), yOf(readings[li])))
            drawCircle(lineColor.copy(alpha = 0.25f), strokeWidth.toPx() * 4f, Offset(xOf(li), yOf(readings[li])))
        }
    }
}