package com.decisionpulse.demo.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.*
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.animation.core.tween

@Composable
fun DashboardScreen(
    onViewFarms: () -> Unit,
    onViewInsights: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val summary by vm.summary.collectAsState()
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        show = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        AnimatedVisibility(show, enter = fadeIn() + slideInVertically { -30 }) {
            Column {
                Text("Good morning,", style = MaterialTheme.typography.bodyLarge)
                Text(summary.name, style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Nyeri County Dairy SACCO · Live Dashboard",
                    style = MaterialTheme.typography.bodySmall,
                    color = DPGreen
                )
            }
        }

        // Main ring + today litres
        AnimatedVisibility(show, enter = fadeIn(tween(600))) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedRing(
                    progress = summary.monthlyProgressPercent,
                    modifier = Modifier.size(210.dp),
                    progressColor = DPGreen,
                    strokeWidth = 26f
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(
                        target = summary.totalLitresToday,
                        decimals = 1,
                        suffix = "L",
                        style = MaterialTheme.typography.displayLarge,
                        color = DPGreen
                    )
                    Text("litres today", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "%.0f%% of monthly target".format(summary.monthlyProgressPercent * 100),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Stat tiles row
        AnimatedVisibility(show, enter = fadeIn(tween(800))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile(
                    label = "On Track", value = summary.farmsOnTrack.toDouble(),
                    accent = DPGreen, modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label = "Watching", value = summary.farmsWatch.toDouble(),
                    accent = DPAmber, modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label = "Critical", value = summary.farmsCritical.toDouble(),
                    accent = DPRed, modifier = Modifier.weight(1f)
                )
            }
        }

        // Cost per litre
        AnimatedVisibility(show, enter = fadeIn(tween(900))) {
            LiveMetricTile(
                label = "Avg Feed Cost / Litre",
                value = summary.avgFeedCostPerLitre,
                suffix = " KES",
                decimals = 2,
                accent = DPBlue,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // CTA Buttons
        AnimatedVisibility(show, enter = fadeIn(tween(1000))) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onViewFarms,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DPGreenDim)
                ) {
                    Icon(Icons.Filled.Agriculture, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("View All Farms", color = DPGreen)
                }
                OutlinedButton(
                    onClick = onViewInsights,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DPBlue.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = DPBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("AI Insights", color = DPBlue)
                }
            }
        }
    }
}
