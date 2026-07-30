package com.foresight.app.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.foresight.app.R
import com.foresight.app.ui.navigation.Screen
import com.foresight.app.ui.navigation.bottomNavItems

@Composable
fun ForesightBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        bottomNavItems.forEach { screen ->
            val label = when (screen.route) {
                Screen.Home.route -> stringResource(R.string.nav_home)
                Screen.Categories.route -> stringResource(R.string.nav_categories)
                Screen.Search.route -> stringResource(R.string.nav_search)
                Screen.Alerts.route -> stringResource(R.string.nav_alerts)
                else -> ""
            }
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
