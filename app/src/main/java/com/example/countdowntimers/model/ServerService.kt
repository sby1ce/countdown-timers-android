package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerService {
    @GET("/")
    suspend fun hello(): Timer
    @POST("/")
    suspend fun insert(timer: Timer)
    @DELETE
    suspend fun delete(timer: Timer)
}
