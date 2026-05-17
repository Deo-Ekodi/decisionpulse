package com.decisionpulse.demo.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.components.ParticleCanvas
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.animation.core.tween

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "SplashAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated particles filling background
        ParticleCanvas(modifier = Modifier.fillMaxSize().alpha(0.6f))

        Column(
            modifier = Modifier.fillMaxSize().alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "DecisionPulse",
                style = MaterialTheme.typography.displayLarge,
                color = TextWhite
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "AI-Powered Agricultural Intelligence",
                style = MaterialTheme.typography.titleMedium,
                color = DPGreen
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Nyeri County · Dairy SACCO Demo",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}
