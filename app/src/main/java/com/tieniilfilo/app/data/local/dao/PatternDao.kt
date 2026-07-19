package com.tieniilfilo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatternDao {
    @Query("SELECT * FROM patterns ORDER BY title ASC")
    fun getAllPatterns(): Flow<List<PatternEntity>>

    @Query("SELECT * FROM patterns WHERE is_bookmarked = 1 ORDER BY title ASC")
    fun getBookmarkedPatterns(): Flow<List<PatternEntity>>

    @Query("SELECT * FROM patterns WHERE type = :type ORDER BY title ASC")
    fun getPatternsByType(type: String): Flow<List<PatternEntity>>

    @Query("SELECT * FROM patterns WHERE id = :id")
    suspend fun getPatternById(id: Long): PatternEntity?

    @Query("SELECT * FROM patterns WHERE id = :id")
    fun getPatternByIdFlow(id: Long): Flow<PatternEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pattern: PatternEntity): Long

    @Update
    suspend fun update(pattern: PatternEntity)

    @Delete
    suspend fun delete(pattern: PatternEntity)

    @Query("DELETE FROM patterns WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM patterns")
    fun getPatternCount(): Flow<Int>

    @Query("SELECT * FROM pattern_photos WHERE pattern_id = :patternId")
    suspend fun getPhotosForPattern(patternId: Long): List<PatternPhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PatternPhotoEntity): Long

    @Delete
    suspend fun deletePhoto(photo: PatternPhotoEntity)

    @Query("DELETE FROM pattern_photos WHERE id = :id")
    suspend fun deletePhotoById(id: Long)

    @Query("DELETE FROM patterns WHERE is_sample = 1")
    suspend fun deleteSampleData()
}
