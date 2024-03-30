package com.fikrilal.githubuserapps.data.response

import com.google.gson.annotations.SerializedName

data class UserFollowersResponse(
	val userFollowersResponse: List<UserFollowersResponseItem?>? = null
)

data class UserFollowersResponseItem(
	val gistsUrl: String? = null,
	val reposUrl: String? = null,
	val followingUrl: String? = null,
	val starredUrl: String? = null,
	val login: String? = null,
	val followersUrl: String? = null,
	val type: String? = null,
	val url: String? = null,
	val subscriptionsUrl: String? = null,
	val receivedEventsUrl: String? = null,
	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,
	val eventsUrl: String? = null,
	val htmlUrl: String? = null,
	val siteAdmin: Boolean? = null,
	val id: Int? = null,
	val gravatarId: String? = null,
	val nodeId: String? = null,
	val organizationsUrl: String? = null
)

