package com.fikrilal.githubuserapps.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fikrilal.githubuserapps.data.response.UsersFav
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDatabase {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun userInsert(usersFav: UsersFav)
    @Delete
    suspend fun userDelete(usersFav: UsersFav)
    @Query("SELECT * FROM UsersFav")
    fun getFavUsers(): Flow<List<UsersFav>>
    @Query("SELECT * FROM UsersFav WHERE username LIKE :username")
    fun searchFavUsers(username: String): Flow<List<UsersFav>>
    @Query("SELECT EXISTS(SELECT * FROM UsersFav WHERE username = :username)")
    suspend fun isFavUsersExist(username: String): Boolean
    @Query("SELECT COUNT(username) FROM UsersFav WHERE username = :username")
    suspend fun countUsers(username: String): Int

}