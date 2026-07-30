package com.foresight.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.foresight.app.ui.screens.addedit.AddEditItemScreen
import com.foresight.app.ui.screens.alerts.AlertsScreen
import com.foresight.app.ui.screens.categories.CategoriesScreen
import com.foresight.app.ui.screens.categories.CategoryDetailScreen
import com.foresight.app.ui.screens.detail.ItemDetailScreen
import com.foresight.app.ui.screens.home.HomeScreen
import com.foresight.app.ui.screens.onboarding.OnboardingScreen
import com.foresight.app.ui.screens.premium.PremiumScreen
import com.foresight.app.ui.screens.search.SearchScreen
import com.foresight.app.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Bottom Nav
        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onAddClick = {
                    navController.navigate(Screen.AddEditItem.createRoute())
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Categories.route) {
            CategoriesScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                }
            )
        }

        composable(Screen.Alerts.route) {
            AlertsScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                }
            )
        }

        // Item Detail
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
            ItemDetailScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Screen.AddEditItem.createRoute(itemId)) }
            )
        }

        // Add/Edit Item
        composable(
            route = Screen.AddEditItem.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId")?.takeIf { it > 0 }
            AddEditItemScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Category Detail
        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: return@composable
            CategoryDetailScreen(
                categoryId = categoryId,
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onPremiumClick = { navController.navigate(Screen.Premium.route) }
            )
        }

        // Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Premium
        composable(Screen.Premium.route) {
            PremiumScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
