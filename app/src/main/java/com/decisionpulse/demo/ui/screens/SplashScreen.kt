package com.decisionpulse.demo.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.components.ParticleCanvas
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label         = "SplashAlpha"
    )
    val scale by animateFloatAsState(
        targetValue   = if (visible) 1f else 0.92f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label         = "SplashScale"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(3200)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ParticleCanvas(modifier = Modifier.fillMaxSize().alpha(0.5f), particleCount = 50)
        Column(
            modifier                = Modifier.fillMaxSize().alpha(alpha).scale(scale),
            horizontalAlignment     = Alignment.CenterHorizontally,
            verticalArrangement     = Arrangement.Center
        ) {
            Text("DecisionPulse", style = MaterialTheme.typography.displayLarge, color = TextWhite)
            Spacer(Modifier.height(10.dp))
            Text("AI-Powered Agricultural Intelligence", style = MaterialTheme.typography.titleMedium, color = DPGreen)
            Spacer(Modifier.height(6.dp))
            Text("Nyeri County  —  Five SACCOs  —  74 Farms", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Spacer(Modifier.height(4.dp))
            Text("decisionpulse.net", style = MaterialTheme.typography.bodySmall, color = TextMuted.copy(alpha = 0.6f))
        }
    }
}