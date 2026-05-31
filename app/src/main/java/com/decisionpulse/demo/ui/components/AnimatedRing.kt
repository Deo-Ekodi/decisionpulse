package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.decisionpulse.demo.ui.theme.Border2
import com.decisionpulse.demo.ui.theme.DPGreen

@Composable
fun AnimatedRing(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Border2,
    progressColor: Color = DPGreen,
    strokeWidth: Float = 24f,
    durationMs: Int = 1400
) {
    var started by remember { mutableStateOf(false) }
    val sweep by animateFloatAsState(
        targetValue   = if (started) progress * 300f else 0f,
        animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
        label         = "RingSweep"
    )
    LaunchedEffect(Unit) { started = true }

    Canvas(modifier = modifier) {
        val inset   = strokeWidth / 2f
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(inset, inset)
        val startAngle = 120f

        drawArc(
            color      = trackColor,
            startAngle = startAngle,
            sweepAngle = 300f,
            useCenter  = false,
            topLeft    = topLeft,
            size       = arcSize,
            style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        if (sweep > 0f) {
            val glowInset = inset - strokeWidth * 0.35f
            drawArc(
                color      = progressColor.copy(alpha = 0.14f),
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter  = false,
                topLeft    = Offset(glowInset, glowInset),
                size       = Size(size.width - glowInset * 2f, size.height - glowInset * 2f),
                style      = Stroke(width = strokeWidth * 1.7f, cap = StrokeCap.Round)
            )
            drawArc(
                color      = progressColor,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter  = false,
                topLeft    = topLeft,
                size       = arcSize,
                style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}