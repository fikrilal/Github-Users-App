package com.fikrilal.githubuserapps.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UsersFav (
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = null,
)
