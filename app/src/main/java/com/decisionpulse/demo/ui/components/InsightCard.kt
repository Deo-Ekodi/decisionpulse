package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.AiInsight
import com.decisionpulse.demo.data.model.InsightPriority
import com.decisionpulse.demo.data.model.InsightType
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun InsightCard(
    insight: AiInsight,
    modifier: Modifier = Modifier,
    animate: Boolean = false,
    entryDelay: Long = 0L
) {
    val priorityColor = when (insight.priority) {
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
                delay(15L)
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
                    modifier         = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(priorityColor.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = priorityColor, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(insight.title, style = MaterialTheme.typography.titleMedium)
                    val label = when {
                        insight.farmCode != null -> insight.farmCode
                        insight.saccoId != null  -> insight.saccoId + " SACCO"
                        else                     -> "All SACCOs"
                    }
                    Text(label, style = MaterialTheme.typography.bodySmall, color = DPBlue)
                }
                StatusBadge(label = insight.priority.name, color = priorityColor)
            }

            Spacer(Modifier.height(12.dp))
            Text(displayedBody, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = Border, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = DPGreen, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text(insight.action, style = MaterialTheme.typography.bodySmall, color = DPGreen, modifier = Modifier.weight(1f))
                Text("${insight.hoursAgo}h ago", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}