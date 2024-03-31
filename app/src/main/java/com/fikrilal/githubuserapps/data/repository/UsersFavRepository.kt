package com.fikrilal.githubuserapps.data.repository

import com.fikrilal.githubuserapps.data.database.UsersDatabase
import com.fikrilal.githubuserapps.data.response.UsersFav
import kotlinx.coroutines.flow.Flow

class UsersFavRepository(private val usersDatabase: UsersDatabase) {
    suspend fun insert(usersFav: UsersFav) = usersDatabase.userInsert(usersFav)
    suspend fun delete(usersFav: UsersFav) = usersDatabase.userDelete(usersFav)
    fun getUsersFav(): Flow<List<UsersFav>> = usersDatabase.getFavUsers()
    fun searchUsersFav(username: String): Flow<List<UsersFav>> {
        return usersDatabase.searchFavUsers(username)
    }
    suspend fun isFavUsersExist(username: String): Boolean = usersDatabase.isFavUsersExist(username)
    suspend fun isUserAdded(username: String): Boolean {
        return usersDatabase.countUsers(username) > 0
    }
}