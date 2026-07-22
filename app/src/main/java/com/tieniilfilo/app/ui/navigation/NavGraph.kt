package com.tieniilfilo.app.ui.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.ui.components.AnimatedFab
import com.tieniilfilo.app.ui.components.DefaultFabState
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
    val scope = rememberCoroutineScope()

    var showYarnAddForm by remember { mutableStateOf(false) }
    var yarnToEdit by remember { mutableStateOf<YarnEntity?>(null) }
    var showHookForm by remember { mutableStateOf(false) }
    var showProjectForm by remember { mutableStateOf(false) }
    var projectToEdit by remember { mutableStateOf<ProjectEntity?>(null) }
    var showPatternForm by remember { mutableStateOf(false) }
    var patternToEdit by remember { mutableStateOf<PatternEntity?>(null) }

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
                            navController.navigate(screen.navRoute) {
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
                    AnimatedFab(
                        state = DefaultFabState,
                        onClick = {
                            when (currentRoute) {
                                Screen.Yarns.route -> showYarnAddForm = true
                                Screen.Hooks.route -> showHookForm = true
                                Screen.Projects.route -> showProjectForm = true
                                Screen.Patterns.route -> showPatternForm = true
                            }
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 } },
                exitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(300)) { -it / 4 } },
                popEnterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 4 } },
                popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(300)) { it / 4 } },
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onYarnClick = { navController.navigate(Screen.Yarns.route) },
                        onProjectClick = { navController.navigate(Screen.Projects.createRoute()) },
                        onPatternClick = { navController.navigate(Screen.Patterns.route) },
                        onGalleryClick = { navController.navigate(Screen.Gallery.route) },
                        onStatsClick = { navController.navigate(Screen.Stats.route) },
                        onActiveProjectsClick = { navController.navigate(Screen.Projects.createRoute("IN_CORSO")) },
                        onCompletedProjectsClick = { navController.navigate(Screen.Projects.createRoute("COMPLETATO")) },
                        onProjectDetailClick = { id -> navController.navigate(Screen.ProjectDetail.createRoute(id)) },
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
                        onAddClick = { showYarnAddForm = true },
                        onPhotoDelete = { yarn -> yarnVm.deletePhoto(yarn) },
                        viewModel = yarnVm,
                    )
                }

                composable(Screen.Hooks.route) {
                    HooksScreen(
                        onAddClick = { showHookForm = true },
                        viewModel = hookVm,
                    )
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
                        onDeleteWithUndo = { msg, undo ->
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(msg, "ANNULLA", duration = SnackbarDuration.Long)
                                if (result == SnackbarResult.ActionPerformed) undo()
                            }
                        },
                        viewModel = yarnVm,
                    )
                }

                composable(
                    route = Screen.Projects.route,
                    arguments = listOf(androidx.navigation.navArgument("status") {
                        type = androidx.navigation.NavType.StringType
                        nullable = true
                        defaultValue = null
                    }),
                ) { backStackEntry ->
                    val statusStr = backStackEntry.arguments?.getString("status")
                    val initialStatus = statusStr?.let {
                        kotlin.runCatching { com.tieniilfilo.app.data.local.entity.ProjectStatus.valueOf(it) }.getOrNull()
                    }
                    ProjectsScreen(
                        onProjectClick = { id ->
                            navController.navigate(Screen.ProjectDetail.createRoute(id))
                        },
                        onAddClick = { showProjectForm = true },
                        initialStatus = initialStatus,
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
                        onEdit = { projectToEdit = it },
                        onDeleteWithUndo = { msg, undo ->
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(msg, "ANNULLA", duration = SnackbarDuration.Long)
                                if (result == SnackbarResult.ActionPerformed) undo()
                            }
                        },
                        viewModel = projectVm,
                    )
                }

                composable(Screen.Patterns.route) {
                    PatternsScreen(
                        onPatternClick = { id ->
                            navController.navigate(Screen.PatternDetail.createRoute(id))
                        },
                        onAddClick = { showPatternForm = true },
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
                        onEdit = { patternToEdit = it },
                        onDeleteWithUndo = { msg, undo ->
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(msg, "ANNULLA", duration = SnackbarDuration.Long)
                                if (result == SnackbarResult.ActionPerformed) undo()
                            }
                        },
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
            isVisible = showProjectForm || projectToEdit != null,
            initialProject = projectToEdit,
            onDismiss = { showProjectForm = false; projectToEdit = null },
            onSave = { name, status, notes, patternId ->
                projectVm.addProject(name, status, notes, patternId)
            },
            onUpdate = { project ->
                projectVm.updateProject(project)
                projectToEdit = null
            },
            allPatterns = patterns,
        )

        PatternFormSheet(
            isVisible = showPatternForm || patternToEdit != null,
            initialPattern = patternToEdit,
            onDismiss = { showPatternForm = false; patternToEdit = null },
            onSave = { title, type, sourceType, fileUri, link, notes ->
                patternVm.addPattern(title, type, sourceType, fileUri, link, notes)
                showPatternForm = false
            },
            onUpdate = { pattern ->
                patternVm.updatePattern(pattern)
                patternToEdit = null
            },
        )
    }
}
