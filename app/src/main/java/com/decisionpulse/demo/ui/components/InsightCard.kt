package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.AiInsight
import com.decisionpulse.demo.data.model.InsightPriority
import com.decisionpulse.demo.data.model.InsightType
import com.decisionpulse.demo.ui.theme.BgSurface2
import com.decisionpulse.demo.ui.theme.Border
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPBlue
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed
import com.decisionpulse.demo.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun InsightCard(
    insight: AiInsight,
    modifier: Modifier = Modifier,
    animate: Boolean = false,
    entryDelay: Long = 0L
) {
    val priorityColor: androidx.compose.ui.graphics.Color = when (insight.priority) {
        InsightPriority.HIGH   -> DPRed
        InsightPriority.MEDIUM -> DPAmber
        InsightPriority.LOW    -> DPGreen
    }
    val icon: ImageVector = when (insight.type) {
        InsightType.ALERT       -> Icons.Filled.Warning
        InsightType.COST        -> Icons.Filled.MonetizationOn
        InsightType.PERFORMANCE -> Icons.Filled.TrendingUp
        InsightType.OPPORTUNITY -> Icons.Filled.Lightbulb
    }

    var displayedBody by remember(insight.id) {
        mutableStateOf(if (animate) "" else insight.body)
    }

    if (animate) {
        LaunchedEffect(insight.id) {
            delay(entryDelay)
            displayedBody = ""
            insight.body.forEachIndexed { i, _ ->
                delay(16L)
                displayedBody = insight.body.substring(0, i + 1)
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = BgSurface2),
        border   = androidx.compose.foundation.BorderStroke(1.dp, priorityColor.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(priorityColor.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector      = icon,
                        contentDescription = null,
                        tint             = priorityColor,
                        modifier         = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text  = insight.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (insight.farmCode != null) {
                        Text(
                            text  = insight.farmCode,
                            style = MaterialTheme.typography.bodySmall,
                            color = DPBlue
                        )
                    } else {
                        Text(
                            text  = "SACCO-wide",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
                StatusBadge(label = insight.priority.name, color = priorityColor)
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text  = displayedBody,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(10.dp))

            HorizontalDivider(color = Border, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector      = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint             = DPGreen,
                    modifier         = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text     = insight.action,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = DPGreen,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text  = "${insight.hoursAgo}h ago",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}