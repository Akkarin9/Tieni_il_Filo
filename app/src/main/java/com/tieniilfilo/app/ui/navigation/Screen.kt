package com.tieniilfilo.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val navRoute: String = route) {
    data object Home : Screen("home")
    data object Yarns : Screen("yarns")
    data object Hooks : Screen("hooks")
    data object Projects : Screen("projects?status={status}", "projects") {
        fun createRoute(status: String? = null) = if (status == null) "projects" else "projects?status=$status"
    }
    data object Patterns : Screen("patterns")
    data object YarnDetail : Screen("yarn_detail/{id}") {
        fun createRoute(id: Long) = "yarn_detail/$id"
    }
    data object ProjectDetail : Screen("project_detail/{id}") {
        fun createRoute(id: Long) = "project_detail/$id"
    }
    data object PatternDetail : Screen("pattern_detail/{id}") {
        fun createRoute(id: Long) = "pattern_detail/$id"
    }
    data object Gallery : Screen("gallery")
    data object Stats : Screen("stats")
    data object Settings : Screen("settings")
}

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String,
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Rounded.Home, "Home"),
    BottomNavItem(Screen.Yarns, Icons.Rounded.Inventory2, "Filati"),
    BottomNavItem(Screen.Projects, Icons.Rounded.Palette, "Progetti"),
    BottomNavItem(Screen.Patterns, Icons.Rounded.AutoStories, "Schemi"),
)
