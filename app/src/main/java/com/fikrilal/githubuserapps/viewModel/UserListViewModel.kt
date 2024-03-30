package com.fikrilal.githubuserapps.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrilal.githubuserapps.data.response.UserFollowersResponseItem
import com.fikrilal.githubuserapps.data.retrofit.ApiConfig
import com.fikrilal.githubuserapps.data.retrofit.ApiService
import com.fikrilal.githubuserapps.util.Event
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private val apiService: ApiService by lazy { ApiConfig.retrofit.create(ApiService::class.java) }

    private val _followerData = MutableLiveData<List<UserFollowersResponseItem>>()
    val followerLiveData: LiveData<List<UserFollowersResponseItem>> get() = _followerData

    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatusLiveData: LiveData<Boolean> get() = _loadingStatus

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessageLiveData: LiveData<Event<String>> get() = _snackbarMessage

    fun fetchUserData(username: String, type: UserType) {
        _loadingStatus.postValue(true)
        log("fetchUserData called with username: $username and type: $type")
        viewModelScope.launch {
            try {
                fetchData(username, type)
            } catch (e: Exception) {
                _snackbarMessage.postValue(Event("An error occurred: ${e.localizedMessage}"))
                logError("Exception fetching data", e)
                _loadingStatus.postValue(false)
            }
        }
    }

    private suspend fun fetchData(username: String, type: UserType) {
        val action = when (type) {
            UserType.FOLLOWERS -> "Fetching followers"
            UserType.FOLLOWING -> "Fetching following"
        }
        log("$action for $username")
        val response = when (type) {
            UserType.FOLLOWERS -> apiService.getFollowers(username)
            UserType.FOLLOWING -> apiService.getFollowing(username)
        }

        if (response.isSuccessful) {
            response.body()?.let {
                _followerData.postValue(it)
                log("Data fetched and posted successfully: ${it.size} items")
            } ?: run {
                logError("Response body is null")
                _snackbarMessage.postValue(Event("Data is null"))
            }
        } else {
            _snackbarMessage.postValue(Event("Error occurred: ${response.message()}"))
            logError("Error fetching data: ${response.message()} with code: ${response.code()}")
        }
        _loadingStatus.postValue(false)
    }

    private fun log(message: String) {
        Log.d("UserListViewModel", message)
    }

    private fun logError(message: String, e: Exception? = null) {
        Log.e("UserListViewModel", message, e)
    }

    enum class UserType {
        FOLLOWERS, FOLLOWING
    }
}