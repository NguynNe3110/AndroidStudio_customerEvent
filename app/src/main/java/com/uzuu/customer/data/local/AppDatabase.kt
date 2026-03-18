package com.uzuu.customer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uzuu.customer.data.local.dao.UsersDao
import com.uzuu.customer.data.local.entity.UsersEntity


@Database(
    entities = [
        UsersEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao() : UsersDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "managerEvent.db"
                )
                    .build()
                INSTANCE =  db
                db
            }
        }
    }
}