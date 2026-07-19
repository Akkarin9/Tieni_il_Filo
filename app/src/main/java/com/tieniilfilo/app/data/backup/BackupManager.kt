package com.tieniilfilo.app.data.backup

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tieniilfilo.app.data.local.dao.HookDao
import com.tieniilfilo.app.data.local.dao.PatternDao
import com.tieniilfilo.app.data.local.dao.ProjectDao
import com.tieniilfilo.app.data.local.dao.YarnDao
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.YarnEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class AppBackup(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val yarns: List<YarnEntity> = emptyList(),
    val hooks: List<HookEntity> = emptyList(),
    val projects: List<ProjectEntity> = emptyList(),
    val patterns: List<PatternEntity> = emptyList(),
)

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val yarnDao: YarnDao,
    private val hookDao: HookDao,
    private val projectDao: ProjectDao,
    private val patternDao: PatternDao,
) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun exportToUri(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val backup = AppBackup(
                yarns = yarnDao.getAllYarns().first(),
                hooks = hookDao.getAllHooks().first(),
                projects = projectDao.getAllProjects().first(),
                patterns = patternDao.getAllPatterns().first(),
            )
            context.contentResolver.openOutputStream(uri)?.use { out ->
                out.write(gson.toJson(backup).toByteArray(Charsets.UTF_8))
            } ?: return@withContext false
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun importFromUri(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader().readText()
            } ?: return@withContext false
            val backup = gson.fromJson(json, AppBackup::class.java) ?: return@withContext false
            backup.yarns.forEach { yarnDao.insert(it.copy(id = 0, isSample = false)) }
            backup.hooks.forEach { hookDao.insert(it.copy(id = 0)) }
            backup.patterns.forEach { patternDao.insert(it.copy(id = 0, isSample = false)) }
            backup.projects.forEach { projectDao.insert(it.copy(id = 0, isSample = false, patternId = null)) }
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun clearSampleData() = withContext(Dispatchers.IO) {
        yarnDao.deleteSampleData()
        projectDao.deleteSampleData()
        patternDao.deleteSampleData()
    }
}
