package com.decisionpulse.demo.ui.screens.farms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.FarmStatusCard
import com.decisionpulse.demo.ui.components.LiveMetricTile
import com.decisionpulse.demo.ui.theme.DPAmber
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed
import com.decisionpulse.demo.ui.theme.TextMuted
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmRosterScreen(
    onFarmSelected: (String) -> Unit,
    onBack: () -> Unit,
    vm: FarmRosterViewModel = viewModel()
) {
    val farms by vm.farms.collectAsState()

    val onTrack  = farms.count { it.status == com.decisionpulse.demo.data.model.FarmStatus.ON_TRACK }
    val watching = farms.count { it.status == com.decisionpulse.demo.data.model.FarmStatus.WATCH }
    val critical = farms.count { it.status == com.decisionpulse.demo.data.model.FarmStatus.CRITICAL }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Farm Roster")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector      = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier        = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding  = PaddingValues(bottom = 24.dp)
        ) {

            // Summary tiles
            item {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LiveMetricTile(
                        label    = "On Track",
                        value    = onTrack.toDouble(),
                        accent   = DPGreen,
                        modifier = Modifier.weight(1f)
                    )
                    LiveMetricTile(
                        label    = "Watching",
                        value    = watching.toDouble(),
                        accent   = DPAmber,
                        modifier = Modifier.weight(1f)
                    )
                    LiveMetricTile(
                        label    = "Critical",
                        value    = critical.toDouble(),
                        accent   = DPRed,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = "${farms.size} farms  —  tap any card for details",
                    style    = MaterialTheme.typography.bodySmall,
                    color    = TextMuted,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                Spacer(Modifier.height(6.dp))
            }

            // Farm cards (staggered entrance)
            itemsIndexed(farms) { index, farm ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 70L)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter   = slideInVertically(tween(320)) { 36 } + fadeIn(tween(320))
                ) {
                    FarmStatusCard(
                        farm    = farm,
                        onClick = { onFarmSelected(farm.code) }
                    )
                }
            }
        }
    }
}