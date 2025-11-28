package com.example.countdowntimers.server

import io.javalin.Javalin

fun main() {
    Javalin.create()
        .get("/") { ctx -> ctx.result("Hello World") }
        .start(7070)
}
