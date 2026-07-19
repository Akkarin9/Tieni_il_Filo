package com.tieniilfilo.app.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tieniilfilo.app.ui.screens.HomeScreen
import com.tieniilfilo.app.ui.screens.gallery.GalleryScreen
import com.tieniilfilo.app.ui.screens.hook.HookFormSheet
import com.tieniilfilo.app.ui.screens.hook.HookViewModel
import com.tieniilfilo.app.ui.screens.hook.HooksScreen
import com.tieniilfilo.app.ui.screens.pattern.PatternDetailScreen
import com.tieniilfilo.app.ui.screens.pattern.PatternFormSheet
import com.tieniilfilo.app.ui.screens.pattern.PatternViewModel
import com.tieniilfilo.app.ui.screens.pattern.PatternsScreen
import com.tieniilfilo.app.ui.screens.project.ProjectDetailScreen
import com.tieniilfilo.app.ui.screens.project.ProjectFormSheet
import com.tieniilfilo.app.ui.screens.project.ProjectViewModel
import com.tieniilfilo.app.ui.screens.project.ProjectsScreen
import com.tieniilfilo.app.ui.screens.settings.SettingsScreen
import com.tieniilfilo.app.ui.screens.stats.StatsScreen
import com.tieniilfilo.app.ui.screens.yarn.YarnDetailScreen
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.ui.screens.yarn.YarnFormSheet
import com.tieniilfilo.app.ui.screens.yarn.YarnViewModel
import com.tieniilfilo.app.ui.screens.yarn.YarnsScreen

@Composable
fun TieniIlFiloNavGraph() {
    Log.d("TIENI", "TieniIlFiloNavGraph rendering")
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }

    var showYarnAddForm by remember { mutableStateOf(false) }
    var yarnToEdit by remember { mutableStateOf<YarnEntity?>(null) }
    var showHookForm by remember { mutableStateOf(false) }
    var showProjectForm by remember { mutableStateOf(false) }
    var showPatternForm by remember { mutableStateOf(false) }

    val yarnVm: YarnViewModel = hiltViewModel()
    val hookVm: HookViewModel = hiltViewModel()
    val projectVm: ProjectViewModel = hiltViewModel()
    val patternVm: PatternViewModel = hiltViewModel()

    val bottomBarRoutes = bottomNavItems.map { it.screen.route }

    fun showFab(): Boolean = currentRoute in listOf(
        Screen.Yarns.route,
        Screen.Hooks.route,
        Screen.Projects.route,
        Screen.Patterns.route,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    TieniIlFiloBottomNav(
                        currentRoute = currentRoute,
                        onItemClick = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            },
            floatingActionButton = {
                if (showFab()) {
                    FloatingActionButton(
                        onClick = {
                            when (currentRoute) {
                                Screen.Yarns.route -> showYarnAddForm = true
                                Screen.Hooks.route -> showHookForm = true
                                Screen.Projects.route -> showProjectForm = true
                                Screen.Patterns.route -> showPatternForm = true
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aggiungi")
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onYarnClick = { navController.navigate(Screen.Yarns.route) },
                        onProjectClick = { navController.navigate(Screen.Projects.route) },
                        onPatternClick = { navController.navigate(Screen.Patterns.route) },
                        onGalleryClick = { navController.navigate(Screen.Gallery.route) },
                        onStatsClick = { navController.navigate(Screen.Stats.route) },
                        onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    )
                }

                composable(Screen.Yarns.route) {
                    YarnsScreen(
                        onYarnClick = { id ->
                            navController.navigate(Screen.YarnDetail.createRoute(id))
                        },
                        onNavigateToHooks = {
                            navController.navigate(Screen.Hooks.route)
                        },
                        viewModel = yarnVm,
                    )
                }

                composable(Screen.Hooks.route) {
                    HooksScreen(viewModel = hookVm)
                }

                composable(
                    route = Screen.YarnDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.LongType }),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id") ?: 0L
                    YarnDetailScreen(
                        yarnId = id,
                        onBack = { navController.popBackStack() },
                        onEdit = { yarn -> yarnToEdit = yarn },
                        viewModel = yarnVm,
                    )
                }

                composable(Screen.Projects.route) {
                    ProjectsScreen(
                        onProjectClick = { id ->
                            navController.navigate(Screen.ProjectDetail.createRoute(id))
                        },
                        viewModel = projectVm,
                    )
                }

                composable(
                    route = Screen.ProjectDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.LongType }),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id") ?: 0L
                    ProjectDetailScreen(
                        projectId = id,
                        onBack = { navController.popBackStack() },
                        onPatternClick = { patternId ->
                            navController.navigate(Screen.PatternDetail.createRoute(patternId))
                        },
                        viewModel = projectVm,
                    )
                }

                composable(Screen.Patterns.route) {
                    PatternsScreen(
                        onPatternClick = { id ->
                            navController.navigate(Screen.PatternDetail.createRoute(id))
                        },
                        viewModel = patternVm,
                    )
                }

                composable(
                    route = Screen.PatternDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.LongType }),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id") ?: 0L
                    PatternDetailScreen(
                        patternId = id,
                        onBack = { navController.popBackStack() },
                        viewModel = patternVm,
                    )
                }

                composable(Screen.Gallery.route) {
                    GalleryScreen()
                }

                composable(Screen.Stats.route) {
                    StatsScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(onBack = { navController.popBackStack() })
                }
            }
        }

        YarnFormSheet(
            isVisible = showYarnAddForm || yarnToEdit != null,
            initialYarn = yarnToEdit,
            onDismiss = { showYarnAddForm = false; yarnToEdit = null },
            onSave = { name, brand, colorName, colorHex, colorHexes, composition, customComposition, qtyBalls, qtyGrams, qtyMeters, yarnSource, storeName, storeLink, notes, photo ->
                yarnVm.addYarn(name, brand, colorName, colorHex, colorHexes, composition, customComposition, qtyBalls, qtyGrams, qtyMeters, yarnSource, storeName, storeLink, notes, photo)
                showYarnAddForm = false
            },
            onUpdate = { yarn ->
                yarnVm.updateYarn(yarn)
                yarnToEdit = null
            },
        )

        HookFormSheet(
            isVisible = showHookForm,
            onDismiss = { showHookForm = false },
            onSave = { size, material, brand ->
                hookVm.addHook(size, material, brand)
            },
        )

        val patterns by patternVm.allPatterns.collectAsState()

        ProjectFormSheet(
            isVisible = showProjectForm,
            onDismiss = { showProjectForm = false },
            onSave = { name, status, notes, patternId ->
                projectVm.addProject(name, status, notes, patternId)
            },
            allPatterns = patterns,
        )

        PatternFormSheet(
            isVisible = showPatternForm,
            onDismiss = { showPatternForm = false },
            onSave = { title, type, sourceType, fileUri, link, notes ->
                patternVm.addPattern(title, type, sourceType, fileUri, link, notes)
            },
        )
    }
}
