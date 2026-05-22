package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.Farm
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.theme.BgSurface2
import com.decisionpulse.demo.ui.theme.Border2
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed

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
        initialValue  = 1f,
        targetValue   = 1.5f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseDot"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape  = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface2),
        border = BorderStroke(1.dp, Border2)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            // Left status bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                // Header row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pulse dot
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(18.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(statusColor.copy(alpha = 0.18f))
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                    }

                    Text(
                        text  = farm.code,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    StatusBadge(label = statusLabel, color = statusColor)
                }

                Spacer(Modifier.height(2.dp))
                Text(
                    text  = farm.subLocation,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text  = "%.1fL/day".format(farm.currentLitresPerDay),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor
                        )
                        Text(
                            text  = "Target: %.0fL  —  %.0f%%".format(
                                farm.targetLitresPerDay,
                                farm.progressPercent * 100
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    SparklineChart(
                        data     = farm.weeklyHistory,
                        modifier = Modifier.size(width = 72.dp, height = 36.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress    = { farm.progressPercent },
                    modifier    = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color       = statusColor,
                    trackColor  = Border2
                )
            }
        }
    }
}