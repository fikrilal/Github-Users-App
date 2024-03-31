package com.fikrilal.githubuserapps.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fikrilal.githubuserapps.data.repository.UsersFavRepository

class UserFavViewModelFac(private val repository: UsersFavRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}