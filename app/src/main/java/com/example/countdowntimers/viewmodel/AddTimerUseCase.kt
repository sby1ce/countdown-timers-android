package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.model.TimerRepository
import javax.inject.Inject

class AddTimerUseCase @Inject constructor(private val repository: TimerRepository) {
    suspend operator fun invoke(name: String, dateMillis: Long, hour: Int, minute: Int): Boolean {
        if (name.isEmpty()) throw IllegalArgumentException("Name empty")
        return repository.addTimer(name, dateMillis, hour, minute)
    }
}
