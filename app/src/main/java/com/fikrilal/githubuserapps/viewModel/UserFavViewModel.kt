package com.fikrilal.githubuserapps.viewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrilal.githubuserapps.data.database.UsersFavDb
import com.fikrilal.githubuserapps.data.repository.UsersFavRepository
import com.fikrilal.githubuserapps.data.response.UsersFav
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserFavViewModel(private val repository: UsersFavRepository) : ViewModel() {
    private val _userFav = MutableStateFlow<List<UsersFav>>(emptyList())
    val userFavLiveData: StateFlow<List<UsersFav>> = _userFav.asStateFlow()

    init {
        Log.d("UserFavViewModel", "Initializing ViewModel")
        loadUserFav()
    }

    private fun loadUserFav() = viewModelScope.launch {
        Log.d("UserFavViewModel", "Loading user favorites from repository")
        try {
            repository.getUsersFav().collect { users ->
                _userFav.value = users
                Log.d("UserFavViewModel", "User favorites loaded successfully")
            }
        } catch (e: Exception) {
            Log.e("UserFavViewModel", "Error loading user favorites: ${e.message}")
        }
    }

    companion object {
        fun create(context: Context): UserFavViewModel {
            Log.d("UserFavViewModel", "Creating ViewModel instance")
            val userDatabase = UsersFavDb.getDatabase(context).usersDatabase()
            val repository = UsersFavRepository(userDatabase)
            return UserFavViewModel(repository)
        }
    }
}