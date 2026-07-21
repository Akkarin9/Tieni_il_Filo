package com.tieniilfilo.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tieniilfilo.app.ui.TieniIlFiloAppContent
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        applyLocale()
        super.onCreate(savedInstanceState)
        setContent {
            TieniIlFiloAppContent()
        }
    }

    private fun applyLocale() {
        val prefs = getSharedPreferences("tieni_il_filo_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "it") ?: "it"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        // Also update base context for the activity
        val updatedConfig = Configuration(config)
        baseContext.resources.updateConfiguration(updatedConfig, baseContext.resources.displayMetrics)
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("tieni_il_filo_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "it") ?: "it"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
}
