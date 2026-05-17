package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.animation.core.tween
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed

@Composable
fun SparklineChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    durationMs: Int = 900
) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMs, easing = FastOutSlowInEasing),
        label = "Sparkline"
    )
    LaunchedEffect(Unit) { started = true }
    if (data.size < 2) return

    val trending = data.last() >= data.first()
    val color = if (trending) DPGreen else DPRed

    Canvas(modifier = modifier) {
        val max = data.max(); val min = data.min()
        val range = (max - min).takeIf { it > 0 } ?: 1.0
        val path = Path().apply {
            data.forEachIndexed { i, v ->
                val x = size.width * i / (data.size - 1)
                val y = size.height - ((v - min) / range * size.height).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        val pm = PathMeasure()
        pm.setPath(path, false)
        val seg = Path()
        pm.getSegment(0f, pm.length * progress, seg, true)
        drawPath(seg, color, style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
