package com.example.countdowntimers.server

import com.google.gson.Gson
import io.javalin.Javalin
import io.javalin.http.bodyAsClass
import io.javalin.json.JavalinGson

data class Timer(
    val key: Int,
    val name: String,
    val origin: Long,
) {
    fun into(userId: String): TimerDto {
        return TimerDto(this.key, this.name, this.origin, userId)
    }
}

data class TimerDto(
    val key: Int,
    val name: String,
    val origin: Long,
    val userId: String,
) {
    fun into(): Timer {
        return Timer(this.key, this.name, this.origin)
    }
}

fun main() {
    val repo = TimerRepository()

    Javalin.create { config ->
        config.jsonMapper(JavalinGson(Gson()))
    }
        .get("/") { ctx ->
            val userId: String? = ctx.header("X-User-Id")
            if (userId.isNullOrEmpty()) {
                ctx.status(401).result("Missing user ID")
                return@get
            }
            val timers = repo.findAll(userId)
            ctx.json(timers)
        }
        .post("/") { ctx ->
            val userId: String? = ctx.header("X-User-Id")
            if (userId.isNullOrEmpty()) {
                ctx.status(401).result("Missing user ID")
                return@post
            }
            val timer = ctx.bodyAsClass<Timer>().into(userId)
            val created = repo.create(timer)
            ctx.status(201).json(created)
        }
        .delete("/{id}") { ctx ->
            val userId: String? = ctx.header("X-User-Id")
            if (userId.isNullOrEmpty()) {
                ctx.status(401).result("Missing user ID")
                return@delete
            }
            val id = ctx.pathParam("id").toInt()
            val deleted = repo.delete(userId, id)
            if (deleted) {
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        }
        .start(7070)
}
