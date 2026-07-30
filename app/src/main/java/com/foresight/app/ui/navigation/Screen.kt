package com.foresight.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titleResId: Int = 0, val icon: ImageVector? = null) {
    // Bottom nav
    data object Home : Screen("home", com.foresight.app.R.string.nav_home, Icons.Default.Home)
    data object Categories : Screen("categories", com.foresight.app.R.string.nav_categories, Icons.Default.Category)
    data object Search : Screen("search", com.foresight.app.R.string.nav_search, Icons.Default.Search)
    data object Alerts : Screen("alerts", com.foresight.app.R.string.nav_alerts, Icons.Default.Notifications)

    // Detail screens
    data object ItemDetail : Screen("item/{itemId}", titleResId = 0) {
        fun createRoute(itemId: Long) = "item/$itemId"
    }
    data object AddEditItem : Screen("add_edit_item?itemId={itemId}", titleResId = 0) {
        fun createRoute(itemId: Long? = null): String {
            return if (itemId != null) "add_edit_item?itemId=$itemId"
            else "add_edit_item"
        }
    }

    // Other screens
    data object Settings : Screen("settings", com.foresight.app.R.string.settings, Icons.Default.Settings)
    data object Onboarding : Screen("onboarding")
    data object Premium : Screen("premium", com.foresight.app.R.string.premium_title)
    data object CategoryDetail : Screen("category/{categoryId}", titleResId = 0) {
        fun createRoute(categoryId: Long) = "category/$categoryId"
    }
}

// Bottom navigation items
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Categories,
    Screen.Search,
    Screen.Alerts,
)
