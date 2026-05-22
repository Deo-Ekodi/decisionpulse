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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed

@Composable
fun SparklineChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    durationMs: Int = 900
) {
    if (data.size < 2) return

    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue   = if (started) 1f else 0f,
        animationSpec = tween(durationMs, easing = FastOutSlowInEasing),
        label         = "Sparkline"
    )
    LaunchedEffect(Unit) { started = true }

    val trending = data.last() >= data.first()
    val color    = if (trending) DPGreen else DPRed
    val maxVal   = data.max()
    val minVal   = data.min()
    val range    = (maxVal - minVal).coerceAtLeast(0.1)

    Canvas(modifier = modifier) {
        fun xOf(i: Int)    = size.width * i / (data.size - 1).coerceAtLeast(1)
        fun yOf(v: Double) = size.height - ((v - minVal) / range * size.height * 0.85f).toFloat()

        // Smooth bezier path
        val fullPath = Path().apply {
            data.forEachIndexed { i, v ->
                val x = xOf(i); val y = yOf(v)
                if (i == 0) {
                    moveTo(x, y)
                } else {
                    val cpX = (xOf(i - 1) + x) / 2f
                    cubicTo(cpX, yOf(data[i - 1]), cpX, y, x, y)
                }
            }
        }

        val pm          = PathMeasure()
        pm.setPath(fullPath, false)
        val clippedPath = Path()
        pm.getSegment(0f, pm.length * progress, clippedPath, true)

        // Gradient fill
        val lastIdx  = ((data.size - 1) * progress).toInt().coerceIn(0, data.size - 1)
        val fillPath = Path().apply {
            addPath(clippedPath)
            lineTo(xOf(lastIdx), size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path  = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.18f), color.copy(alpha = 0f))
            )
        )

        // Line
        drawPath(
            path  = clippedPath,
            color = color,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // End dot
        if (progress > 0.97f) {
            drawCircle(color = color, radius = 3.5f, center = Offset(xOf(data.size - 1), yOf(data.last())))
        }
    }
}