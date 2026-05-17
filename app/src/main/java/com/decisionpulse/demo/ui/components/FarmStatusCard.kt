package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.tween
import com.decisionpulse.demo.data.model.Farm
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.theme.*

@Composable
fun FarmStatusCard(
    farm: Farm,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusColor, statusLabel) = when (farm.status) {
        FarmStatus.ON_TRACK -> DPGreen to "On Track"
        FarmStatus.WATCH    -> DPAmber to "Watch"
        FarmStatus.CRITICAL -> DPRed   to "Critical"
    }

    // Pulsing dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface2),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border2)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status pulse dot
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.22f))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(farm.code, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    StatusBadge(label = statusLabel, color = statusColor)
                }
                Spacer(Modifier.height(2.dp))
                Text(farm.subLocation, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "%.1fL/day".format(farm.currentLitresPerDay),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor
                        )
                        Text(
                            "Target: %.0fL — %.0f%%".format(
                                farm.targetLitresPerDay,
                                farm.progressPercent * 100
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    SparklineChart(
                        data = farm.weeklyHistory,
                        modifier = Modifier.size(width = 72.dp, height = 36.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Linear progress bar
                LinearProgressIndicator(
                    progress = { farm.progressPercent },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = statusColor,
                    trackColor = Border2
                )
            }
        }
    }
}
