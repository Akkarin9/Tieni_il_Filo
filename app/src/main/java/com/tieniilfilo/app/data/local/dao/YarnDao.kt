package com.tieniilfilo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tieniilfilo.app.data.local.entity.YarnEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YarnDao {
    @Query("SELECT * FROM yarns WHERE is_wishlist = 0 ORDER BY name ASC")
    fun getAllYarns(): Flow<List<YarnEntity>>

    @Query("SELECT * FROM yarns WHERE id = :id")
    suspend fun getYarnById(id: Long): YarnEntity?

    @Query("SELECT * FROM yarns WHERE id = :id")
    fun getYarnByIdFlow(id: Long): Flow<YarnEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(yarn: YarnEntity): Long

    @Update
    suspend fun update(yarn: YarnEntity)

    @Delete
    suspend fun delete(yarn: YarnEntity)

    @Query("DELETE FROM yarns WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM yarns WHERE is_wishlist = 1 ORDER BY name ASC")
    fun getWishlistYarns(): Flow<List<YarnEntity>>

    @Query("SELECT COUNT(*) FROM yarns WHERE status = :status AND is_wishlist = 0")
    suspend fun countByStatus(status: String): Int

    @Query("SELECT COUNT(*) FROM yarns WHERE is_wishlist = 0")
    fun getYarnCount(): Flow<Int>

    @Query("SELECT color_hex FROM yarns WHERE color_hex != 0 AND is_wishlist = 0 GROUP BY color_hex")
    suspend fun getDistinctColors(): List<Int>

    @Query("DELETE FROM yarns WHERE is_sample = 1")
    suspend fun deleteSampleData()

    @Query("UPDATE yarns SET status = :status WHERE id IN (SELECT yarn_id FROM project_yarn_cross_ref WHERE project_id = :projectId)")
    suspend fun updateYarnStatusByProject(projectId: Long, status: String)

    @Query("SELECT * FROM yarns WHERE id IN (SELECT yarn_id FROM project_yarn_cross_ref WHERE project_id = :projectId)")
    suspend fun getYarnsForProject(projectId: Long): List<YarnEntity>

    @Query("SELECT COALESCE(SUM(unit_price), 0) FROM yarns WHERE unit_price IS NOT NULL AND is_wishlist = 0")
    suspend fun getTotalPrice(): Double
}
