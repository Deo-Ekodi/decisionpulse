package com.decisionpulse.demo.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.decisionpulse.demo.ui.theme.*

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

private val navItems = listOf(
    BottomNavItem(Screen.Dashboard,  "Dashboard", Icons.Filled.Dashboard),
    BottomNavItem(Screen.FarmRoster, "Farms",     Icons.Filled.Agriculture),
    BottomNavItem(Screen.Insights,   "Insights",  Icons.Filled.Lightbulb)
)

@Composable
fun DPBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(containerColor = BgSurface, tonalElevation = 0.dp) {
        navItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick  = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon   = { Icon(item.icon, contentDescription = item.label) },
                label  = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = DPGreen,
                    selectedTextColor   = DPGreen,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor      = DPGreenDim
                )
            )
        }
    }
}