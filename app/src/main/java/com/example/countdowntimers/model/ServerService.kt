package com.example.countdowntimers.model

import retrofit2.http.GET

interface ServerService {
    @GET("/")
    suspend fun hello(): String
}
