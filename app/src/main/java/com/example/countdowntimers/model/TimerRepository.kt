package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.hashName
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.collections.plus

private fun getOrigin(dateMillis: Long, hour: Int, minute: Int): Long {
    return dateMillis + hour * 60 * 60 * 1000 + minute * 60 * 1000
}

class TimerRepository(init: List<Timer>) {
    companion object {
        val mutex = Mutex()
        var state: List<Timer> = emptyList()
    }

    init {
        state = init
    }

    private fun hasName(name: String): Boolean {
        return state.any { timer -> timer.name == name }
    }

    suspend fun addTimer(
        name: String, dateMillis: Long, hour: Int, minute: Int,
    ): Boolean = mutex.withLock {
        if (hasName(name)) {
            return false
        }
        state = state + Timer(
            key = hashName(name),
            name = name,
            origin = getOrigin(dateMillis, hour, minute),
        )
        true
    }

    suspend fun popTimer(id: Int) = mutex.withLock {
        state = state.filter { timer -> timer.key != id }
    }

    suspend fun getTimers(): List<Timer> = mutex.withLock {
        state
    }
}
