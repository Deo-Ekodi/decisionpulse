package com.decisionpulse.demo.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.AnimatedRing
import com.decisionpulse.demo.ui.components.CountingNumber
import com.decisionpulse.demo.ui.components.LiveMetricTile
import com.decisionpulse.demo.ui.components.MonthlyBarsChart
import com.decisionpulse.demo.ui.theme.BgSurface2
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPBlue
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPGreenDim
import com.decisionpulse.demo.ui.theme.DPRed
import com.decisionpulse.demo.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onViewFarms: () -> Unit,
    onViewInsights: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val summary         by vm.summary.collectAsState()
    val coordinatorName = vm.coordinatorName
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(80)
        show = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // ── Header ──────────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn() + slideInVertically { -28 }
        ) {
            Column {
                Text(
                    text  = "Good morning,",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text  = coordinatorName,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text  = "${summary.name}  —  Coordinator Dashboard",
                    style = MaterialTheme.typography.bodySmall,
                    color = DPGreen
                )
            }
        }

        // ── Main ring ───────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn(tween(550))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier.fillMaxWidth()
            ) {
                AnimatedRing(
                    progress      = summary.monthlyProgressPercent,
                    modifier      = Modifier.size(210.dp),
                    progressColor = DPGreen,
                    strokeWidth   = 26f
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(
                        target   = summary.totalLitresToday,
                        decimals = 1,
                        suffix   = "L",
                        style    = MaterialTheme.typography.displayLarge,
                        color    = DPGreen
                    )
                    Text(
                        text  = "litres today",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text      = "%.0f%% of monthly target".format(
                            summary.monthlyProgressPercent * 100
                        ),
                        style     = MaterialTheme.typography.bodySmall,
                        color     = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Stat tiles ──────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn(tween(750))
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile(
                    label    = "On Track",
                    value    = summary.farmsOnTrack.toDouble(),
                    accent   = DPGreen,
                    modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label    = "Watching",
                    value    = summary.farmsWatch.toDouble(),
                    accent   = DPAmber,
                    modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label    = "Critical",
                    value    = summary.farmsCritical.toDouble(),
                    accent   = DPRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ── Feed cost ───────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn(tween(850))
        ) {
            LiveMetricTile(
                label    = "Avg Feed Cost / Litre",
                value    = summary.avgFeedCostPerLitre,
                suffix   = " KES",
                decimals = 2,
                accent   = DPBlue,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ── Monthly trend ───────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn(tween(950))
        ) {
            Card(
                colors   = CardDefaults.cardColors(containerColor = BgSurface2),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text  = "12-Month Production Trend",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text  = "Average litres / day across all farms",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(14.dp))
                    MonthlyBarsChart(
                        data     = summary.monthlyHistory,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                    )
                }
            }
        }

        // ── CTA buttons ─────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = show,
            enter   = fadeIn(tween(1050))
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick  = onViewFarms,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = DPGreenDim),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Agriculture, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("View All Farms", color = DPGreen)
                }
                OutlinedButton(
                    onClick  = onViewInsights,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border   = androidx.compose.foundation.BorderStroke(
                        1.dp, DPBlue.copy(alpha = 0.35f)
                    ),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = DPBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("AI Insights", color = DPBlue)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}