package com.tieniilfilo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tieniilfilo.app.data.local.entity.HookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HookDao {
    @Query("SELECT * FROM hooks ORDER BY size_mm ASC")
    fun getAllHooks(): Flow<List<HookEntity>>

    @Query("SELECT * FROM hooks WHERE id = :id")
    suspend fun getHookById(id: Long): HookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hook: HookEntity): Long

    @Update
    suspend fun update(hook: HookEntity)

    @Delete
    suspend fun delete(hook: HookEntity)

    @Query("DELETE FROM hooks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM hooks")
    fun getHookCount(): Flow<Int>

    @Query("SELECT * FROM hooks WHERE id IN (SELECT hook_id FROM project_hook_cross_ref WHERE project_id = :projectId)")
    suspend fun getHooksForProject(projectId: Long): List<HookEntity>
}
