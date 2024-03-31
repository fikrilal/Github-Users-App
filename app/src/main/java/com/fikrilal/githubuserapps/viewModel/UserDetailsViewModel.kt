package com.fikrilal.githubuserapps.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrilal.githubuserapps.data.database.UsersFavDb
import com.fikrilal.githubuserapps.data.repository.UsersFavRepository
import com.fikrilal.githubuserapps.data.response.UserDetailsResponse
import com.fikrilal.githubuserapps.data.response.UsersFav
import com.fikrilal.githubuserapps.data.retrofit.ApiConfig
import com.fikrilal.githubuserapps.data.retrofit.ApiService
import com.fikrilal.githubuserapps.util.Event
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val repository: UsersFavRepository) : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetailsResponse>()
    val userDetail: LiveData<UserDetailsResponse> get() = _userDetail

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage

    private val apiService: ApiService by lazy { ApiConfig.retrofit.create(ApiService::class.java) }

    fun fetchUserDetails(username: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUserDetail(username)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        _userDetail.postValue(responseBody)
                        _snackbarMessage.value = Event("Account details fetched for $username")
                    } ?: run {
                        _snackbarMessage.value = Event("No details found for $username")
                    }
                } else {
                    _snackbarMessage.value = Event("Error occurred: ${response.message()}")
                }
            } catch (e: Exception) {
                _snackbarMessage.value = Event("Error occurred: ${e.localizedMessage}")
            }
        }
    }

    private val _isUserFavorited = MutableLiveData<Boolean>()
    val isUserFavoritedLiveData: LiveData<Boolean> = _isUserFavorited

    fun userExistCheck(username: String) = viewModelScope.launch {
        val isAdded = repository.isUserAdded(username)
        _isUserFavorited.postValue(isAdded)
    }
    fun addUsers(usersFav: UsersFav) = viewModelScope.launch {
        repository.insert(usersFav)
        userExistCheck(usersFav.username)
    }
    fun delUser(usersFav: UsersFav) = viewModelScope.launch {
        repository.delete(usersFav)
        userExistCheck(usersFav.username)
    }

    companion object {
        fun create(context: Context): UserDetailsViewModel {
            val userDatabase = UsersFavDb.getDatabase(context).usersDatabase()
            val  userFavRepository = UsersFavRepository(userDatabase)
            return UserDetailsViewModel(userFavRepository)
        }
    }
}