package com.tieniilfilo.app.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.local.SeedManager
import com.tieniilfilo.app.ui.navigation.TieniIlFiloNavGraph
import com.tieniilfilo.app.ui.screens.onboarding.OnboardingScreen
import com.tieniilfilo.app.ui.theme.TieniIlFiloTheme
import com.tieniilfilo.app.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val seedManager: SeedManager,
) : ViewModel() {
    init { Log.d("TIENI", "AppViewModel created") }

    val onboardingDone = preferences.onboardingDone
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val darkMode = preferences.darkMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val useDynamicColors = preferences.useDynamicColors
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val language = preferences.language
        .stateIn(viewModelScope, SharingStarted.Eagerly, "it")

    fun completeOnboarding() {
        preferences.setOnboardingDone(true)
        viewModelScope.launch { seedManager.seedIfNeeded() }
    }
}

@Composable
fun TieniIlFiloAppContent(
    viewModel: AppViewModel = hiltViewModel(),
) {
    val onboardingDone by viewModel.onboardingDone.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val language by viewModel.language.collectAsState()

    Log.d("TIENI", "TieniIlFiloAppContent recompose — onboardingDone=$onboardingDone")

    TieniIlFiloTheme(darkTheme = darkMode, dynamicColor = useDynamicColors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Crossfade(
                targetState = onboardingDone,
                animationSpec = tween(500),
                label = "onboardingCrossfade",
            ) { done ->
                if (done) {
                    Log.d("TIENI", "Rendering NavGraph")
                    TieniIlFiloNavGraph()
                } else {
                    OnboardingScreen(onFinished = viewModel::completeOnboarding)
                }
            }
        }
    }
}
