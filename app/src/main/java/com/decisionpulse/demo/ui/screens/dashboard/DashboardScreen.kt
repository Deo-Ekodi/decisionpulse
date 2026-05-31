package com.decisionpulse.demo.ui.screens.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.AppView
import com.decisionpulse.demo.ui.AppState
import com.decisionpulse.demo.ui.components.*
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onViewFarms: () -> Unit,
    onViewInsights: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val summary by vm.summary.collectAsState()
    var show    by remember { mutableStateOf(false) }

    // React to SACCO selection changes
    LaunchedEffect(AppState.selectedSaccoId) {
        vm.loadSacco(AppState.selectedSaccoId)
    }
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

        // ── View Switcher ────────────────────────────────────────────────
        AnimatedVisibility(show, enter = fadeIn()) {
            ViewSwitcher(
                current  = AppState.currentView,
                onSelect = { AppState.currentView = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ── Render the appropriate view ──────────────────────────────────
        AnimatedContent(
            targetState = AppState.currentView,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "ViewSwitch"
        ) { view ->
            when (view) {
                AppView.FARMER           -> FarmerDashboard(show = show)
                AppView.EXTENSION_OFFICER -> OfficerDashboard(
                    show           = show,
                    summary        = summary,
                    onViewFarms    = onViewFarms,
                    onViewInsights = onViewInsights
                )
                AppView.EXECUTIVE        -> ExecutiveDashboard(show = show)
            }
        }
    }
}

// ── FARMER VIEW ─────────────────────────────────────────────────────────────

@Composable
private fun FarmerDashboard(show: Boolean) {
    val farm = remember { MockRepository.getFarmByCode(MockRepository.demoFarmerCode) }
    if (farm == null) return

    val statusColor = DPGreen

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        AnimatedVisibility(show, enter = fadeIn() + slideInVertically { -28 }) {
            Column {
                Text("Welcome back,", style = MaterialTheme.typography.bodyLarge)
                Text(farm.farmerName, style = MaterialTheme.typography.headlineLarge)
                Text("${farm.code}  •  ${farm.subLocation}", style = MaterialTheme.typography.bodySmall, color = DPGreen)
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(550))) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedRing(progress = farm.progressPercent, modifier = Modifier.size(200.dp), progressColor = statusColor, strokeWidth = 24f)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(target = farm.currentLitresPerDay, decimals = 1, suffix = "L", style = MaterialTheme.typography.displayLarge, color = statusColor)
                    Text("litres today", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("%.0f%% of your target".format(farm.progressPercent * 100), style = MaterialTheme.typography.bodySmall, color = TextMuted, textAlign = TextAlign.Center)
                }
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(700))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile("Feed Cost/L", farm.feedCostPerLitre, " KES", 2, DPBlue, Modifier.weight(1f))
                LiveMetricTile("Consistency", farm.consistencyScore.toDouble(), "/100", 0, DPPurple, Modifier.weight(1f))
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(850))) {
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your 7-Day Trend", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    SparklineChart(data = farm.weeklyHistory, modifier = Modifier.fillMaxWidth().height(72.dp))
                }
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(950))) {
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Your Farm", style = MaterialTheme.typography.titleMedium)
                    FarmDetailRow("Breed", farm.breed.display)
                    FarmDetailRow("Lactating cows", "${farm.lactatingCount} of ${farm.cowCount}")
                    FarmDetailRow("Milking frequency", "${farm.milkingFrequency}x daily")
                    FarmDetailRow("Fodder", farm.fodderSource)
                    FarmDetailRow("Water", farm.waterSource)
                    FarmDetailRow("Labour model", farm.labourModel)
                    FarmDetailRow("Target", "${farm.targetLitresPerDay.toInt()}L/day by ${MockRepository.getSaccoById(farm.saccoId)?.targetYear ?: 2027}")
                    FarmDetailRow("Projection", farm.projectedDate)
                }
            }
        }
    }
}

@Composable
private fun FarmDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
    }
}

// ── EXTENSION OFFICER VIEW ───────────────────────────────────────────────────

@Composable
private fun OfficerDashboard(
    show: Boolean,
    summary: com.decisionpulse.demo.data.model.SaccoSummary,
    onViewFarms: () -> Unit,
    onViewInsights: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        // SACCO selector
        AnimatedVisibility(show, enter = fadeIn(tween(200))) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("SACCO", style = MaterialTheme.typography.labelSmall)
                SaccoPicker(
                    saccos     = MockRepository.saccos,
                    selectedId = AppState.selectedSaccoId,
                    onSelect   = { AppState.selectedSaccoId = it },
                    modifier   = Modifier.fillMaxWidth()
                )
            }
        }

        // Header
        AnimatedVisibility(show, enter = fadeIn() + slideInVertically { -28 }) {
            Column {
                Text("Good morning,", style = MaterialTheme.typography.bodyLarge)
                Text(MockRepository.coordinatorName, style = MaterialTheme.typography.headlineLarge)
                Text("${summary.name}  —  Coordinator Dashboard", style = MaterialTheme.typography.bodySmall, color = DPGreen)
            }
        }

        // Main ring
        AnimatedVisibility(show, enter = fadeIn(tween(550))) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedRing(progress = summary.monthlyProgressPercent, modifier = Modifier.size(210.dp), progressColor = DPGreen, strokeWidth = 26f)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(target = summary.totalLitresToday, decimals = 1, suffix = "L", style = MaterialTheme.typography.displayLarge, color = DPGreen)
                    Text("litres today", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("%.0f%% of monthly target".format(summary.monthlyProgressPercent * 100), style = MaterialTheme.typography.bodySmall, color = TextMuted, textAlign = TextAlign.Center)
                }
            }
        }

        // Status tiles
        AnimatedVisibility(show, enter = fadeIn(tween(750))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile("On Track",  summary.farmsOnTrack.toDouble(),  accent = DPGreen, modifier = Modifier.weight(1f))
                LiveMetricTile("Watching",  summary.farmsWatch.toDouble(),    accent = DPAmber, modifier = Modifier.weight(1f))
                LiveMetricTile("Critical",  summary.farmsCritical.toDouble(), accent = DPRed,   modifier = Modifier.weight(1f))
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(850))) {
            LiveMetricTile("Avg Feed Cost / Litre", summary.avgFeedCostPerLitre, " KES", 2, DPBlue, Modifier.fillMaxWidth())
        }

        AnimatedVisibility(show, enter = fadeIn(tween(950))) {
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("12-Month Production Trend", style = MaterialTheme.typography.titleMedium)
                    Text("Average litres / day across member farms", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(Modifier.height(14.dp))
                    MonthlyBarsChart(data = summary.monthlyHistory, modifier = Modifier.fillMaxWidth().height(72.dp))
                }
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(1050))) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick  = onViewFarms,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = DPGreenDim),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Agriculture, null)
                    Spacer(Modifier.width(8.dp))
                    Text("View All Farms", color = DPGreen)
                }
                OutlinedButton(
                    onClick  = onViewInsights,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, DPBlue.copy(alpha = 0.35f)),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Lightbulb, null, tint = DPBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("AI Insights", color = DPBlue)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

// ── EXECUTIVE VIEW ───────────────────────────────────────────────────────────

@Composable
private fun ExecutiveDashboard(show: Boolean) {
    val network  = remember { MockRepository.getNetworkSummary() }
    val saccos   = MockRepository.saccos
    val allFarms = MockRepository.farms

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        AnimatedVisibility(show, enter = fadeIn() + slideInVertically { -28 }) {
            Column {
                Text("Executive Overview", style = MaterialTheme.typography.bodyLarge)
                Text("Nyeri County Network", style = MaterialTheme.typography.headlineLarge)
                Text("${saccos.size} SACCOs  •  ${allFarms.size} farms tracked", style = MaterialTheme.typography.bodySmall, color = DPPurple)
            }
        }

        // Network ring
        AnimatedVisibility(show, enter = fadeIn(tween(550))) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedRing(progress = network.monthlyProgressPercent, modifier = Modifier.size(210.dp), progressColor = DPPurple, strokeWidth = 26f)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(target = network.totalLitresToday, decimals = 0, suffix = "L", style = MaterialTheme.typography.displayLarge, color = DPPurple)
                    Text("total litres today", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("across ${allFarms.size} farms", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(700))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile("On Track",  network.farmsOnTrack.toDouble(),  accent = DPGreen, modifier = Modifier.weight(1f))
                LiveMetricTile("Watch",     network.farmsWatch.toDouble(),    accent = DPAmber, modifier = Modifier.weight(1f))
                LiveMetricTile("Critical",  network.farmsCritical.toDouble(), accent = DPRed,   modifier = Modifier.weight(1f))
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(800))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile("SACCOs", saccos.size.toDouble(), accent = DPPurple, modifier = Modifier.weight(1f))
                LiveMetricTile("Avg KES/L", network.avgFeedCostPerLitre, " KES", 2, DPBlue, Modifier.weight(1f))
            }
        }

        // Per-SACCO breakdown
        AnimatedVisibility(show, enter = fadeIn(tween(900))) {
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SACCO Performance Matrix", style = MaterialTheme.typography.titleMedium)
                    Text("Production vs. target, by cooperative", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(Modifier.height(14.dp))
                    saccos.forEach { sacco ->
                        val saccoFarms = MockRepository.getFarmsBySacco(sacco.id)
                        val avgLitres  = if (saccoFarms.isEmpty()) 0.0 else saccoFarms.sumOf { it.currentLitresPerDay } / saccoFarms.size
                        val progress   = (avgLitres / sacco.targetLitresPerDayPerFarm).toFloat().coerceIn(0f, 1f)
                        val onTrack    = saccoFarms.count { it.status == com.decisionpulse.demo.data.model.FarmStatus.ON_TRACK }
                        val critical   = saccoFarms.count { it.status == com.decisionpulse.demo.data.model.FarmStatus.CRITICAL }
                        val barColor   = when {
                            progress >= 0.85f -> DPGreen
                            progress >= 0.65f -> DPAmber
                            else             -> DPRed
                        }
                        SaccoMatrixRow(
                            name       = sacco.id,
                            fullName   = sacco.name,
                            progress   = progress,
                            avgLitres  = avgLitres,
                            target     = sacco.targetLitresPerDayPerFarm,
                            onTrack    = onTrack,
                            critical   = critical,
                            farms      = saccoFarms.size,
                            color      = barColor
                        )
                        if (sacco != saccos.last()) HorizontalDivider(color = Border, modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        AnimatedVisibility(show, enter = fadeIn(tween(1000))) {
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Network Trend (Avg L/day)", style = MaterialTheme.typography.titleMedium)
                    Text("12-month composite across all five SACCOs", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(Modifier.height(12.dp))
                    MonthlyBarsChart(data = network.monthlyHistory, barColor = DPPurple, modifier = Modifier.fillMaxWidth().height(72.dp))
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SaccoMatrixRow(
    name: String,
    fullName: String,
    progress: Float,
    avgLitres: Double,
    target: Double,
    onTrack: Int,
    critical: Int,
    farms: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, color = color)
                Text(fullName, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("%.1fL / %.0fL target".format(avgLitres, target), style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                Text("$onTrack on track  •  $critical critical  •  $farms farms", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress   = { progress },
            modifier   = Modifier.fillMaxWidth().height(5.dp).androidx.compose.ui.draw.clip(androidx.compose.foundation.shape.RoundedCornerShape(3.dp)),
            color      = color,
            trackColor = com.decisionpulse.demo.ui.theme.Border2
        )
        Spacer(Modifier.height(4.dp))
        Text("%.0f%% of target".format(progress * 100), style = MaterialTheme.typography.bodySmall, color = color)
    }
}