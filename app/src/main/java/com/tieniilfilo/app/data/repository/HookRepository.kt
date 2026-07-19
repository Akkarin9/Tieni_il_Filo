package com.tieniilfilo.app.data.repository

import com.tieniilfilo.app.data.local.dao.HookDao
import com.tieniilfilo.app.data.local.entity.HookEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HookRepository @Inject constructor(
    private val hookDao: HookDao,
) {
    fun getAllHooks(): Flow<List<HookEntity>> = hookDao.getAllHooks()

    suspend fun getHookById(id: Long): HookEntity? = hookDao.getHookById(id)

    suspend fun insert(hook: HookEntity): Long = hookDao.insert(hook)

    suspend fun update(hook: HookEntity) = hookDao.update(hook)

    suspend fun delete(hook: HookEntity) = hookDao.delete(hook)

    suspend fun deleteById(id: Long) = hookDao.deleteById(id)

    fun getCount(): Flow<Int> = hookDao.getHookCount()

    suspend fun getHooksForProject(projectId: Long): List<HookEntity> =
        hookDao.getHooksForProject(projectId)
}
