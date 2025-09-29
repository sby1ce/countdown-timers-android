package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.hashName
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import kotlin.collections.plus

private fun getOrigin(dateMillis: Long, hour: Int, minute: Int): Long {
    return dateMillis + hour * 60 * 60 * 1000 + minute * 60 * 1000
}

data class TimerModel(private val state: List<Timer>) {
    fun hasName(name: String): Boolean {
        return state.any { timer -> timer.name == name }
    }

    fun addTimer(
        name: String, dateMillis: Long, hour: Int, minute: Int,
    ): TimerModel {
        return TimerModel(state + Timer(
            key = hashName(name),
            name = name,
            origin = getOrigin(dateMillis, hour, minute),
        ))
    }

    fun popTimer(idx: Int): TimerModel {
        return TimerModel(state.filterIndexed { index, _ -> index != idx })
    }

    fun render(): List<List<String>> {
        return ktTimers(origins(state))
    }

    fun names(): List<String> {
        return state.map { timer -> timer.name }
    }
}
