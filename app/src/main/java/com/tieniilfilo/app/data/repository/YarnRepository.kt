package com.tieniilfilo.app.data.repository

import com.tieniilfilo.app.data.local.dao.YarnDao
import com.tieniilfilo.app.data.local.entity.YarnEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YarnRepository @Inject constructor(
    private val yarnDao: YarnDao,
) {
    fun getAllYarns(): Flow<List<YarnEntity>> = yarnDao.getAllYarns()

    suspend fun getYarnById(id: Long): YarnEntity? = yarnDao.getYarnById(id)

    fun getYarnByIdFlow(id: Long): Flow<YarnEntity?> = yarnDao.getYarnByIdFlow(id)

    suspend fun insert(yarn: YarnEntity): Long = yarnDao.insert(yarn)

    suspend fun update(yarn: YarnEntity) = yarnDao.update(yarn)

    suspend fun delete(yarn: YarnEntity) = yarnDao.delete(yarn)

    suspend fun deleteById(id: Long) = yarnDao.deleteById(id)

    fun getWishlist(): Flow<List<YarnEntity>> = yarnDao.getWishlistYarns()

    suspend fun countByStatus(status: String): Int = yarnDao.countByStatus(status)

    fun getCount(): Flow<Int> = yarnDao.getYarnCount()

    suspend fun getDistinctColors(): List<Int> = yarnDao.getDistinctColors()

    suspend fun deleteSampleData() = yarnDao.deleteSampleData()

    suspend fun getYarnsForProject(projectId: Long): List<YarnEntity> =
        yarnDao.getYarnsForProject(projectId)
}
