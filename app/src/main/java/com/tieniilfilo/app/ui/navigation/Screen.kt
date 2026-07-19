package com.tieniilfilo.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Yarns : Screen("yarns")
    data object Hooks : Screen("hooks")
    data object Projects : Screen("projects")
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
    BottomNavItem(Screen.Home, Icons.Default.Home, "Home"),
    BottomNavItem(Screen.Yarns, Icons.Default.Inventory2, "Filati"),
    BottomNavItem(Screen.Projects, Icons.Default.Spa, "Progetti"),
    BottomNavItem(Screen.Patterns, Icons.AutoMirrored.Filled.LibraryBooks, "Schemi"),
)
