package com.example.countdowntimers.server

import com.google.gson.Gson
import io.javalin.Javalin
import io.javalin.http.bodyAsClass
import io.javalin.json.JavalinGson


data class Timer(
    val id: Int,
    val name: String,
    val origin: Long,
)

fun main() {
    Javalin.create { config ->
        config.jsonMapper(JavalinGson(Gson()))
    }
        .get("/") { ctx -> ctx.json(Timer(0, "DEF", 0L)) }
        .post("/") { ctx ->
            val timer = ctx.bodyAsClass<Timer>()
            println(timer)
            ctx.status(201)
        }
        .delete("/") { ctx ->
            val timer = ctx.bodyAsClass<Timer>()
            println(timer)
            ctx.status(200)
        }
        .start(7070)
}
