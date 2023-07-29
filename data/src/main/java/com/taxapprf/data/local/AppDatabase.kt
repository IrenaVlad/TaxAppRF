package com.taxapprf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taxapprf.data.local.dao.AccountDao
import com.taxapprf.data.local.dao.MainDao
import com.taxapprf.data.local.dao.UserDao
import com.taxapprf.data.local.entity.AccountEntity
import com.taxapprf.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun mainDao(): MainDao
}