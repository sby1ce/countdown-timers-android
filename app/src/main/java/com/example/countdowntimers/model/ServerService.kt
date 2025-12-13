package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class UserIdInterceptor(private val userId: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-User-Id", userId)
            .build()
        return chain.proceed(request)
    }
}

interface ServerService {
    @GET("/")
    suspend fun select(): List<Timer>
    @POST("/")
    suspend fun insert(@Body timer: Timer)
    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: Int)
}
