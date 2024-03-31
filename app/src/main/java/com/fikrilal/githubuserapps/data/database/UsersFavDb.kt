package com.fikrilal.githubuserapps.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fikrilal.githubuserapps.data.response.UsersFav

@Database(entities = [UsersFav::class], version = 1)
abstract class UsersFavDb : RoomDatabase() {
    abstract fun usersDatabase(): UsersDatabase
    companion object {
        @Volatile
        private var INSTANCE: UsersFavDb? = null
        fun getDatabase(context: Context): UsersFavDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersFavDb::class.java,
                    "user_fav_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}