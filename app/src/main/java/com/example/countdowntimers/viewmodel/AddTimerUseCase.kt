package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerRepository
import com.example.countdowntimers.lib.hashName
import javax.inject.Inject

private fun getOrigin(dateMillis: Long, hour: Int, minute: Int): Long {
    return dateMillis + hour * 60 * 60 * 1000 + minute * 60 * 1000
}

class AddTimerUseCase @Inject constructor(private val repository: TimerRepository) {
    suspend operator fun invoke(
        name: String,
        dateMillis: Long,
        hour: Int,
        minute: Int,
    ): Boolean {
        if (name.isEmpty()) throw IllegalArgumentException("Name empty")
        val timer =
            Timer(hashName(name), name, getOrigin(dateMillis, hour, minute))
        return repository.insertTimer(timer)
    }
}
