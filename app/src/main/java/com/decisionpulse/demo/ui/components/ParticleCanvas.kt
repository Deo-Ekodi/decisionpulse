package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.decisionpulse.demo.ui.theme.DPBlue
import com.decisionpulse.demo.ui.theme.DPGreen
import kotlin.math.*
import kotlin.random.Random

private data class Particle(
    val startX: Float, val startY: Float,
    val vx: Float, val vy: Float,
    val radius: Float, val color: Color,
    val phase: Float
)

@Composable
fun ParticleCanvas(modifier: Modifier = Modifier, particleCount: Int = 55) {
    val particles = remember {
        List(particleCount) {
            val colors = listOf(DPGreen, DPBlue, DPGreen.copy(alpha = .6f), DPBlue.copy(alpha = .5f))
            Particle(
                startX = Random.nextFloat(),
                startY = Random.nextFloat(),
                vx     = (Random.nextFloat() - 0.5f) * 0.12f,
                vy     = (Random.nextFloat() - 0.5f) * 0.12f,
                radius = Random.nextFloat() * 3.5f + 1f,
                color  = colors.random(),
                phase  = Random.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(16_000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = modifier) {
        particles.forEach { p ->
            val t = time * 0.002f
            val x = ((p.startX + p.vx * t + sin(t * 0.7f + p.phase) * 0.04f) % 1f + 1f) % 1f
            val y = ((p.startY + p.vy * t + cos(t * 0.5f + p.phase) * 0.04f) % 1f + 1f) % 1f
            drawCircle(color = p.color, radius = p.radius, center = Offset(x * size.width, y * size.height))
        }
    }
}