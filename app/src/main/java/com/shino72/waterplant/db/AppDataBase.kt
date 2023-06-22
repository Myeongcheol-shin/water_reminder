package com.shino72.waterplant.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PlantPicture::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun PlantDao() : PlantDao
    companion object {
        private var instance : AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}