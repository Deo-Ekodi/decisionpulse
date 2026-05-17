package com.decisionpulse.demo.ui.screens.farmdetail

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.components.*
import com.decisionpulse.demo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(
    farmCode: String,
    onBack: () -> Unit
) {
    val vm: FarmDetailViewModel = viewModel()
    LaunchedEffect(farmCode) { vm.load(farmCode) }

    val farm by vm.farm.collectAsState()
    val history by vm.history.collectAsState()

    val farm_ = farm ?: return

    val statusColor = when (farm_.status) {
        FarmStatus.ON_TRACK -> DPGreen
        FarmStatus.WATCH    -> DPAmber
        FarmStatus.CRITICAL -> DPRed
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(farm_.code) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Sub-location + status
            Row {
                Text(farm_.subLocation, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                StatusBadge(label = farm_.status.name, color = statusColor)
            }

            // Big number
            CountingNumber(
                target = farm_.currentLitresPerDay,
                suffix = "L/day",
                decimals = 1,
                style = MaterialTheme.typography.displayLarge,
                color = statusColor
            )
            Text(
                farm_.projectedDate,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            // 90-day trajectory chart
            Text("90-Day Trajectory", style = MaterialTheme.typography.titleMedium)
            Card(
                colors = CardDefaults.cardColors(containerColor = BgSurface2),
                modifier = Modifier.fillMaxWidth()
            ) {
                TrajectoryChart(
                    readings = history.map { it.litres },
                    target = farm_.targetLitresPerDay,
                    lineColor = statusColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(12.dp)
                )
            }

            // Progress ring + key stats side by side
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(modifier = Modifier.size(120.dp)) {
                    AnimatedRing(
                        progress = farm_.progressPercent,
                        modifier = Modifier.fillMaxSize(),
                        progressColor = statusColor,
                        strokeWidth = 16f
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text("%.0f%%".format(farm_.progressPercent * 100),
                            style = MaterialTheme.typography.headlineMedium, color = statusColor)
                        Text("to goal", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LiveMetricTile("Feed Cost/L", farm_.feedCostPerLitre, " KES", decimals = 2, accent = DPBlue, modifier = Modifier.fillMaxWidth())
                    LiveMetricTile("Consistency", farm_.consistencyScore.toDouble(), "/100", accent = DPPurple, modifier = Modifier.fillMaxWidth())
                }
            }

            // 7-day sparkline
            Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2)) {
                SparklineChart(
                    data = farm_.weeklyHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(12.dp)
                )
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}