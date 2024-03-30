package com.fikrilal.githubuserapps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrilal.githubuserapps.data.response.UserListResponse
import com.fikrilal.githubuserapps.data.retrofit.ApiConfig
import com.fikrilal.githubuserapps.data.retrofit.ApiService
import com.fikrilal.githubuserapps.util.Event
import kotlinx.coroutines.launch

class UserSearchViewModel : ViewModel() {

    private val apiService: ApiService by lazy { ApiConfig.retrofit.create(ApiService::class.java) }

    private val _userSearchResult = MutableLiveData<UserListResponse>()
    val userSearchResult: LiveData<UserListResponse> get() = _userSearchResult

    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatusLiveData: LiveData<Boolean> get() = _loadingStatus

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessageLiveData: LiveData<Event<String>> get() = _snackbarMessage

    fun userSearch(query: String) {
        _loadingStatus.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.userSearch(query)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        _userSearchResult.postValue(responseBody)
                        _loadingStatus.postValue(false)
                        if (query != "a") {
                            _snackbarMessage.postValue(Event("Search results for $query"))
                        }
                    } ?: run {
                        _snackbarMessage.postValue(Event("No data found"))
                        _loadingStatus.postValue(false)
                    }
                } else {
                    _snackbarMessage.postValue(Event("Error occurred: ${response.message()}"))
                    _loadingStatus.postValue(false)
                }
            } catch (e: Exception) {
                _snackbarMessage.postValue(Event("Error occurred: ${e.localizedMessage}"))
                _loadingStatus.postValue(false)
            }
        }
    }
}