package com.decisionpulse.demo.ui.screens.farmdetail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.components.AnimatedRing
import com.decisionpulse.demo.ui.components.CountingNumber
import com.decisionpulse.demo.ui.components.InsightCard
import com.decisionpulse.demo.ui.components.LiveMetricTile
import com.decisionpulse.demo.ui.components.SparklineChart
import com.decisionpulse.demo.ui.components.StatusBadge
import com.decisionpulse.demo.ui.components.TrajectoryChart
import com.decisionpulse.demo.ui.theme.BgSurface2
import com.decisionpulse.demo.ui.theme.Border2
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPBlue
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPPurple
import com.decisionpulse.demo.ui.theme.DPRed
import com.decisionpulse.demo.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(
    farmCode: String,
    onBack: () -> Unit
) {
    val vm: FarmDetailViewModel = viewModel()
    LaunchedEffect(farmCode) { vm.load(farmCode) }

    val farm    by vm.farm.collectAsState()
    val history by vm.history.collectAsState()
    val insight by vm.insight.collectAsState()

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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

            // Location + status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text     = farm_.subLocation,
                    style    = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(label = farm_.status.name, color = statusColor)
            }

            // Current yield headline
            CountingNumber(
                target   = farm_.currentLitresPerDay,
                suffix   = "L/day",
                decimals = 1,
                style    = MaterialTheme.typography.displayLarge,
                color    = statusColor
            )
            Text(
                text  = farm_.projectedDate,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            // 90-day trajectory chart
            Text("90-Day Trajectory", style = MaterialTheme.typography.titleMedium)
            Card(
                colors   = CardDefaults.cardColors(containerColor = BgSurface2),
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp)
            ) {
                TrajectoryChart(
                    readings    = history.map { it.litres },
                    target      = farm_.targetLitresPerDay,
                    lineColor   = statusColor,
                    modifier    = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(16.dp)
                )
            }

            // Progress ring + key stats
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(120.dp)) {
                    AnimatedRing(
                        progress      = farm_.progressPercent,
                        modifier      = Modifier.fillMaxSize(),
                        progressColor = statusColor,
                        strokeWidth   = 16f
                    )
                    Column(
                        modifier                = Modifier.fillMaxSize(),
                        verticalArrangement     = Arrangement.Center,
                        horizontalAlignment     = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text  = "%.0f%%".format(farm_.progressPercent * 100),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor
                        )
                        Text("to goal", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LiveMetricTile(
                        label    = "Feed Cost/L",
                        value    = farm_.feedCostPerLitre,
                        suffix   = " KES",
                        decimals = 2,
                        accent   = DPBlue,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LiveMetricTile(
                        label    = "Consistency",
                        value    = farm_.consistencyScore.toDouble(),
                        suffix   = "/100",
                        accent   = DPPurple,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 7-day sparkline
            Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
            Card(
                colors   = CardDefaults.cardColors(containerColor = BgSurface2),
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp)
            ) {
                SparklineChart(
                    data     = farm_.weeklyHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(14.dp)
                )
            }

            // Farm-specific AI insight (only if one exists)
            if (insight != null) {
                Text("AI Insight", style = MaterialTheme.typography.titleMedium)
                InsightCard(
                    insight    = insight!!,
                    animate    = true,
                    entryDelay = 700L
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}