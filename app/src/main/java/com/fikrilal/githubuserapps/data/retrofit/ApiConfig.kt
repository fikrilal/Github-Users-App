package com.fikrilal.githubuserapps.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "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")
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}