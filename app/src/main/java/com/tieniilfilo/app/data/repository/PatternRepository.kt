package com.tieniilfilo.app.data.repository

import com.tieniilfilo.app.data.local.dao.PatternDao
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternPhotoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatternRepository @Inject constructor(
    private val patternDao: PatternDao,
) {
    fun getAllPatterns(): Flow<List<PatternEntity>> = patternDao.getAllPatterns()

    fun getBookmarkedPatterns(): Flow<List<PatternEntity>> =
        patternDao.getBookmarkedPatterns()

    fun getPatternsByType(type: String): Flow<List<PatternEntity>> =
        patternDao.getPatternsByType(type)

    suspend fun getPatternById(id: Long): PatternEntity? = patternDao.getPatternById(id)

    fun getPatternByIdFlow(id: Long): Flow<PatternEntity?> =
        patternDao.getPatternByIdFlow(id)

    suspend fun insert(pattern: PatternEntity): Long = patternDao.insert(pattern)

    suspend fun update(pattern: PatternEntity) = patternDao.update(pattern)

    suspend fun delete(pattern: PatternEntity) = patternDao.delete(pattern)

    suspend fun deleteById(id: Long) = patternDao.deleteById(id)

    fun getCount(): Flow<Int> = patternDao.getPatternCount()

    suspend fun getPhotos(patternId: Long): List<PatternPhotoEntity> =
        patternDao.getPhotosForPattern(patternId)

    suspend fun insertPhoto(photo: PatternPhotoEntity): Long =
        patternDao.insertPhoto(photo)

    suspend fun deletePhoto(photo: PatternPhotoEntity) = patternDao.deletePhoto(photo)

    suspend fun deletePhotoById(id: Long) = patternDao.deletePhotoById(id)

    suspend fun deleteSampleData() = patternDao.deleteSampleData()
}
