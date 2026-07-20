package com.tieniilfilo.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yarns")
data class YarnEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String = "",
    @ColumnInfo(name = "color_name") val colorName: String = "",
    @ColumnInfo(name = "color_hex") val colorHex: Int = 0,
    val composition: YarnComposition = YarnComposition.ALTRO,
    @ColumnInfo(name = "custom_composition") val customComposition: String? = null,
    @ColumnInfo(name = "quantity_balls_total") val quantityBallsTotal: Double = 0.0,
    @ColumnInfo(name = "quantity_grams_total") val quantityGramsTotal: Double = 0.0,
    @ColumnInfo(name = "quantity_meters_total") val quantityMetersTotal: Double = 0.0,
    @ColumnInfo(name = "quantity_used") val quantityUsed: Double = 0.0,
    val status: YarnStatus = YarnStatus.DISPONIBILE,
    @ColumnInfo(name = "color_hexes") val colorHexes: String? = null,
    @ColumnInfo(name = "photo_uri") val photoUri: String? = null,
    val notes: String = "",
    @ColumnInfo(name = "is_wishlist") val isWishlist: Boolean = false,
    @ColumnInfo(name = "is_sample") val isSample: Boolean = false,
    @ColumnInfo(name = "yarn_source") val yarnSource: YarnSource = YarnSource.NEGOZIO_FISICO,
    @ColumnInfo(name = "store_name") val storeName: String = "",
    @ColumnInfo(name = "store_link") val storeLink: String = "",
    @ColumnInfo(name = "unit_price") val unitPrice: Double? = null,
    @ColumnInfo(name = "purchase_date") val purchaseDate: Long? = null,
)
