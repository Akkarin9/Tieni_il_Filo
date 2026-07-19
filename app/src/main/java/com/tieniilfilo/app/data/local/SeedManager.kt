package com.tieniilfilo.app.data.local

import android.content.Context
import com.tieniilfilo.app.data.local.dao.YarnDao
import com.tieniilfilo.app.util.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val database: AppDatabase,
) {
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        if (preferencesManager.isSeeded()) return@withContext
        DatabaseSeeder.seed(database)
        preferencesManager.setSeeded(true)
    }
}
