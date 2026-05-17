package com.decisionpulse.demo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.decisionpulse.demo.ui.screens.dashboard.DashboardScreen
import com.decisionpulse.demo.ui.screens.farmdetail.FarmDetailScreen
import com.decisionpulse.demo.ui.screens.farms.FarmRosterScreen
import com.decisionpulse.demo.ui.screens.insights.InsightsScreen
import com.decisionpulse.demo.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onViewFarms = { navController.navigate(Screen.FarmRoster.route) },
                onViewInsights = { navController.navigate(Screen.Insights.route) }
            )
        }

        composable(Screen.FarmRoster.route) {
            FarmRosterScreen(
                onFarmSelected = { farmCode ->
                    navController.navigate(Screen.FarmDetail.createRoute(farmCode))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.FarmDetail.route,
            arguments = listOf(navArgument("farmCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val farmCode = backStackEntry.arguments?.getString("farmCode") ?: ""
            FarmDetailScreen(farmCode = farmCode, onBack = { navController.popBackStack() })
        }

        composable(Screen.Insights.route) {
            InsightsScreen(onBack = { navController.popBackStack() })
        }
    }
}