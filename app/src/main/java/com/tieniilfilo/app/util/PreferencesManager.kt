package com.tieniilfilo.app.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _onboardingDone = MutableStateFlow(prefs.getBoolean(KEY_ONBOARDING, false))
    val onboardingDone: StateFlow<Boolean> = _onboardingDone.asStateFlow()

    private val _darkMode = MutableStateFlow(prefs.getBoolean(KEY_DARK_MODE, false))
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _useDynamicColors = MutableStateFlow(prefs.getBoolean(KEY_DYNAMIC_COLORS, false))
    val useDynamicColors: StateFlow<Boolean> = _useDynamicColors.asStateFlow()

    fun setOnboardingDone(done: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING, done).apply()
        _onboardingDone.value = done
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
        _darkMode.value = enabled
    }

    fun setUseDynamicColors(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DYNAMIC_COLORS, enabled).apply()
        _useDynamicColors.value = enabled
    }

    fun isSeeded(): Boolean = prefs.getBoolean(KEY_SEEDED, false)

    fun setSeeded(seeded: Boolean) {
        prefs.edit().putBoolean(KEY_SEEDED, seeded).apply()
    }

    companion object {
        private const val PREFS_NAME = "tieni_il_filo_prefs"
        private const val KEY_ONBOARDING = "onboarding_done"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_DYNAMIC_COLORS = "dynamic_colors"
        private const val KEY_SEEDED = "seeded_v1"
    }
}
