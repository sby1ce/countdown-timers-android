package com.example.countdowntimers.server

import com.google.gson.Gson
import io.javalin.Javalin
import io.javalin.http.bodyAsClass
import io.javalin.json.JavalinGson

data class Timer(
    val key: Int,
    val name: String,
    val origin: Long,
)

fun main() {
    val repo = TimerRepository()

    Javalin.create { config ->
        config.jsonMapper(JavalinGson(Gson()))
    }
        .get("/") { ctx ->
            val timers = repo.findAll()
            ctx.json(timers)
        }
        .post("/") { ctx ->
            val timer = ctx.bodyAsClass<Timer>()
            val created = repo.create(timer)
            ctx.status(201).json(created)
        }
        .delete("/{id}") { ctx ->
            val id = ctx.pathParam("id").toInt()
            val deleted = repo.delete(id)
            if (deleted) {
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        }
        .start(7070)
}
