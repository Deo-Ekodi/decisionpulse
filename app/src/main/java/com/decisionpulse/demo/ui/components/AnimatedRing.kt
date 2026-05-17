package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.animation.core.tween
import com.decisionpulse.demo.ui.theme.*

@Composable
fun AnimatedRing(
    progress: Float,           // 0f to 1f
    modifier: Modifier = Modifier,
    trackColor: Color = Border2,
    progressColor: Color = DPGreen,
    strokeWidth: Float = 24f,
    durationMs: Int = 1400
) {
    var started by remember { mutableStateOf(false) }
    val sweep by animateFloatAsState(
        targetValue = if (started) progress * 300f else 0f,   // 300° max arc
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "RingSweep"
    )

    LaunchedEffect(Unit) { started = true }

    Canvas(modifier = modifier) {
        val inset = strokeWidth / 2
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(inset, inset)
        val startAngle = 120f

        // Track (background)
        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = 300f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress
        if (sweep > 0f) {
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
