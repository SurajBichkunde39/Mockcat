package com.github.mockcat.remote

import com.github.mockcat.data.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/users/{userId}")
    suspend fun getUser(@Path("userId") id: Int): ApiResponse
}
