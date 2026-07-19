package com.tieniilfilo.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pattern_photos",
    foreignKeys = [
        ForeignKey(
            entity = PatternEntity::class,
            parentColumns = ["id"],
            childColumns = ["pattern_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("pattern_id")],
)
data class PatternPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "pattern_id") val patternId: Long,
    @ColumnInfo(name = "photo_uri") val photoUri: String,
)
