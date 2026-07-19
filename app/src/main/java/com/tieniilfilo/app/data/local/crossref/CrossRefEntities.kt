package com.tieniilfilo.app.data.local.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.YarnEntity

@Entity(
    tableName = "project_yarn_cross_ref",
    primaryKeys = ["project_id", "yarn_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = YarnEntity::class,
            parentColumns = ["id"],
            childColumns = ["yarn_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("yarn_id")],
)
data class ProjectYarnCrossRef(
    @ColumnInfo(name = "project_id") val projectId: Long,
    @ColumnInfo(name = "yarn_id") val yarnId: Long,
)

@Entity(
    tableName = "project_hook_cross_ref",
    primaryKeys = ["project_id", "hook_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = HookEntity::class,
            parentColumns = ["id"],
            childColumns = ["hook_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("hook_id")],
)
data class ProjectHookCrossRef(
    @ColumnInfo(name = "project_id") val projectId: Long,
    @ColumnInfo(name = "hook_id") val hookId: Long,
)
