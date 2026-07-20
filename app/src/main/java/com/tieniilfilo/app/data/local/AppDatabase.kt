package com.tieniilfilo.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tieniilfilo.app.data.local.converter.Converters
import com.tieniilfilo.app.data.local.crossref.ProjectHookCrossRef
import com.tieniilfilo.app.data.local.crossref.ProjectYarnCrossRef
import com.tieniilfilo.app.data.local.dao.HookDao
import com.tieniilfilo.app.data.local.dao.PatternDao
import com.tieniilfilo.app.data.local.dao.ProjectDao
import com.tieniilfilo.app.data.local.dao.YarnDao
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternPhotoEntity
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectPhotoEntity
import com.tieniilfilo.app.data.local.entity.YarnEntity

@Database(
    entities = [
        YarnEntity::class,
        HookEntity::class,
        ProjectEntity::class,
        ProjectPhotoEntity::class,
        PatternEntity::class,
        PatternPhotoEntity::class,
        ProjectYarnCrossRef::class,
        ProjectHookCrossRef::class,
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun yarnDao(): YarnDao
    abstract fun hookDao(): HookDao
    abstract fun projectDao(): ProjectDao
    abstract fun patternDao(): PatternDao

    companion object {
        val MIGRATION_1_2 = Migration(1, 2) { db ->
            db.execSQL("ALTER TABLE yarns ADD COLUMN color_hexes TEXT DEFAULT NULL")
        }

        val MIGRATION_2_3 = Migration(2, 3) { db ->
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS yarns_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    brand TEXT NOT NULL DEFAULT '',
                    color_name TEXT NOT NULL DEFAULT '',
                    color_hex INTEGER NOT NULL DEFAULT 0,
                    composition TEXT NOT NULL DEFAULT 'ALTRO',
                    quantity_balls_total REAL NOT NULL DEFAULT 0.0,
                    quantity_grams_total REAL NOT NULL DEFAULT 0.0,
                    quantity_meters_total REAL NOT NULL DEFAULT 0.0,
                    quantity_used REAL NOT NULL DEFAULT 0.0,
                    status TEXT NOT NULL DEFAULT 'DISPONIBILE',
                    color_hexes TEXT DEFAULT NULL,
                    photo_uri TEXT DEFAULT NULL,
                    notes TEXT NOT NULL DEFAULT '',
                    is_wishlist INTEGER NOT NULL DEFAULT 0,
                    is_sample INTEGER NOT NULL DEFAULT 0,
                    yarn_source TEXT NOT NULL DEFAULT 'NEGOZIO_FISICO',
                    store_name TEXT NOT NULL DEFAULT '',
                    store_link TEXT NOT NULL DEFAULT ''
                )
            """)
            db.execSQL("""
                INSERT INTO yarns_new (
                    id, name, brand, color_name, color_hex, composition,
                    quantity_balls_total, quantity_grams_total, quantity_meters_total, quantity_used,
                    status, color_hexes, photo_uri, notes, is_wishlist, is_sample,
                    yarn_source, store_name, store_link
                )
                SELECT
                    id, name, brand, colorName, color_hex, composition,
                    CASE WHEN quantity_unit = 'GOMITOLI' THEN quantity_total ELSE 0 END,
                    CASE WHEN quantity_unit = 'GRAMMI' THEN quantity_total ELSE 0 END,
                    CASE WHEN quantity_unit = 'METRI' THEN quantity_total ELSE 0 END,
                    CASE WHEN quantity_total > 0 THEN (quantity_used / quantity_total) * 100 ELSE 0 END,
                    status, color_hexes, photo_uri, notes, is_wishlist, is_sample,
                    'NEGOZIO_FISICO', '', ''
                FROM yarns
            """)
            db.execSQL("DROP TABLE yarns")
            db.execSQL("ALTER TABLE yarns_new RENAME TO yarns")
        }

        val MIGRATION_3_4 = Migration(3, 4) { db ->
            db.execSQL("ALTER TABLE yarns ADD COLUMN custom_composition TEXT DEFAULT NULL")
        }

        val MIGRATION_4_5 = Migration(4, 5) { db ->
            db.execSQL("ALTER TABLE yarns ADD COLUMN unit_price REAL DEFAULT NULL")
            db.execSQL("ALTER TABLE yarns ADD COLUMN purchase_date INTEGER DEFAULT NULL")
            db.execSQL("ALTER TABLE projects ADD COLUMN target_deadline INTEGER DEFAULT NULL")
            db.execSQL("ALTER TABLE projects ADD COLUMN rating INTEGER NOT NULL DEFAULT 0")
        }
    }
}
