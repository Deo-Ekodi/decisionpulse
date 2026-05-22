package com.decisionpulse.demo.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.components.ParticleCanvas
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.TextMuted
import com.decisionpulse.demo.ui.theme.TextWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label         = "SplashAlpha"
    )
    val scale by animateFloatAsState(
        targetValue   = if (visible) 1f else 0.92f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label         = "SplashScale"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(3200)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ParticleCanvas(
            modifier      = Modifier.fillMaxSize().alpha(0.5f),
            particleCount = 50
        )

        Column(
            modifier                = Modifier
                .fillMaxSize()
                .alpha(alpha)
                .scale(scale),
            horizontalAlignment     = Alignment.CenterHorizontally,
            verticalArrangement     = Arrangement.Center
        ) {
            Text(
                text  = "DecisionPulse",
                style = MaterialTheme.typography.displayLarge,
                color = TextWhite
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text  = "AI-Powered Agricultural Intelligence",
                style = MaterialTheme.typography.titleMedium,
                color = DPGreen
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text  = "Nyeri County  —  Dairy SACCO",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "decisionpulse.net",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted.copy(alpha = 0.6f)
            )
        }
    }
}