package com.lovorise.app.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.lovorise.app.database.entities.UserEntity

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase>

@Database(entities = [UserEntity::class], version = 1)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

internal const val dbFileName = "lovorise.db"