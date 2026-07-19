package com.tieniilfilo.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hooks")
data class HookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @androidx.room.ColumnInfo(name = "size_mm") val sizeMm: Double,
    val material: HookMaterial = HookMaterial.METALLO,
    val brand: String = "",
    val notes: String = "",
)
