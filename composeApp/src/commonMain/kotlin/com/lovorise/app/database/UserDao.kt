package com.lovorise.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lovorise.app.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<UserEntity>)
//
//    @Query("SELECT * FROM user")
//    suspend fun getAllEpisodes(): List<EpisodeEntity>
//
//    @Query("SELECT * FROM episodes WHERE id == :id")
//    fun getEpisode(id: Int): Flow<EpisodeEntity>
//
//    @Query("DELETE FROM episodes")
//    suspend fun clearAll()
//
//    @Transaction
//    suspend fun clearAndInsertAll(episodes: List<EpisodeEntity>) {
//        clearAll()
//        insertAll(episodes)
//    }
}