package com.fikrilal.githubuserapps.data.retrofit

import com.fikrilal.githubuserapps.data.response.UserDetailsResponse
import com.fikrilal.githubuserapps.data.response.UserFollowersResponseItem
import com.fikrilal.githubuserapps.data.response.UserListResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun userSearch(@Query("q") query: String): Response<UserListResponse>
    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetailsResponse>
    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<UserFollowersResponseItem>>
    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<UserFollowersResponseItem>>
}