package com.decisionpulse.demo.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.decisionpulse.demo.ui.theme.BgSurface
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPGreenDim
import com.decisionpulse.demo.ui.theme.TextMuted
import androidx.compose.material3.MaterialTheme as M3Theme

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

private val navItems = listOf(
    BottomNavItem(Screen.Dashboard,   "Dashboard", Icons.Filled.Dashboard),
    BottomNavItem(Screen.FarmRoster,  "Farms",     Icons.Filled.Agriculture),
    BottomNavItem(Screen.Insights,    "Insights",  Icons.Filled.Lightbulb),
)

@Composable
fun DPBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(
        containerColor = BgSurface,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = M3Theme.typography.labelSmall
                    )
                },
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
