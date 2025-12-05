package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerService {
    @GET("/")
    suspend fun select(): List<Timer>
    @POST("/")
    suspend fun insert(timer: Timer)
    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: Int)
}
