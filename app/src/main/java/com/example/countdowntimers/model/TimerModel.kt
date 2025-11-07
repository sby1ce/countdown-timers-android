package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerRepository
import com.example.countdowntimers.lib.hashName
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import kotlin.collections.plus

private fun getOrigin(dateMillis: Long, hour: Int, minute: Int): Long {
    return dateMillis + hour * 60 * 60 * 1000 + minute * 60 * 1000
}

class TimerModel(private val timerRepository: TimerRepository) {
    suspend fun hasName(name: String): Boolean {
        return timerRepository.hasTimer(hashName(name))
    }

    suspend fun addTimer(
        name: String, dateMillis: Long, hour: Int, minute: Int,
    ): TimerModel {
        timerRepository.insertTimer(Timer(
            key = hashName(name),
            name = name,
            origin = getOrigin(dateMillis, hour, minute),
        ))
        return this
    }

    suspend fun popTimer(idx: Int): TimerModel {
        timerRepository.deleteTimer()
        return this
    }

    fun render(now: Long): List<List<String>> {
        return ktTimers(origins(state), now)
    }

    fun names(): List<String> {
        return state.map { timer -> timer.name }
    }
}
