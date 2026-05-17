package com.decisionpulse.demo.ui.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Dashboard   : Screen("dashboard")
    object FarmRoster  : Screen("farms")
    object FarmDetail  : Screen("farm/{farmCode}") {
        fun createRoute(farmCode: String) = "farm/$farmCode"
    }
    object Insights    : Screen("insights")
}
