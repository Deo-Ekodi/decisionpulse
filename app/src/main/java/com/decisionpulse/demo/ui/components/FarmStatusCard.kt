package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
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

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue  = 1f, targetValue  = 1.5f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label         = "PulseDot"
    )

    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = BgSurface2),
        border   = BorderStroke(1.dp, Border2)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp).fillMaxHeight()
                    .background(statusColor, RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(18.dp)) {
                        Box(Modifier.size(16.dp).scale(pulseScale).clip(CircleShape).background(statusColor.copy(alpha = 0.18f)))
                        Box(Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(farm.code, style = MaterialTheme.typography.titleMedium)
                        Text(farm.farmerName, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    StatusBadge(label = statusLabel, color = statusColor)
                }

                Spacer(Modifier.height(4.dp))
                Text("${farm.subLocation}  •  ${farm.breed.display}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text  = "%.1fL/day".format(farm.currentLitresPerDay),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor
                        )
                        Text(
                            text  = "Target: %.0fL  —  %.0f%%".format(farm.targetLitresPerDay, farm.progressPercent * 100),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    SparklineChart(data = farm.weeklyHistory, modifier = Modifier.size(width = 72.dp, height = 36.dp))
                }

                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress   = { farm.progressPercent },
                    modifier   = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color      = statusColor,
                    trackColor = Border2
                )
            }
        }
    }
}