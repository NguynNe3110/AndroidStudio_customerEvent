package com.uzuu.learn14_1_roomdb_foreignkey.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.ClassDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.UserDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.ClassEntity
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ClassEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun classDao() : ClassDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learn14_1.db"
                )
                    .build()
                INSTANCE = db
                db
            }
        }
    }
}