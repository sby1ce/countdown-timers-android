package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerRepository
import javax.inject.Inject

class PopTimerUseCase @Inject constructor(private val repository: TimerRepository) {
    suspend operator fun invoke(timer: Timer) {
        repository.deleteTimer(timer)
    }
}
