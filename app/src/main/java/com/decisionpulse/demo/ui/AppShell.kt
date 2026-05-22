package com.decisionpulse.demo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.decisionpulse.demo.ui.navigation.DPBottomBar
import com.decisionpulse.demo.ui.navigation.NavGraph
import com.decisionpulse.demo.ui.navigation.Screen
import com.decisionpulse.demo.ui.theme.BgDeep
import com.decisionpulse.demo.ui.theme.Border

@Composable
fun AppShell() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = setOf(
        Screen.Dashboard.route,
        Screen.FarmRoster.route,
        Screen.Insights.route
    )
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        containerColor = BgDeep,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                Column {
                    HorizontalDivider(color = Border, thickness = androidx.compose.ui.unit.Dp(1f))
                    DPBottomBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}