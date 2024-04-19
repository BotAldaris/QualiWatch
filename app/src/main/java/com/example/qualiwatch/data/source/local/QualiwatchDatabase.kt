package com.example.qualiwatch.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [LocalProduct::class, UserPreferences::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QualiwatchDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        @Volatile
        private var Instance: QualiwatchDatabase? = null

        fun getDatabase(context: Context): QualiwatchDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, QualiwatchDatabase::class.java, "qualiwatch_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}