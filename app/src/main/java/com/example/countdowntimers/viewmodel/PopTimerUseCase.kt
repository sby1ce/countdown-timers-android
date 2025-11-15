package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.model.TimerRepository
import javax.inject.Inject

class PopTimerUseCase @Inject constructor(private val repository: TimerRepository) {
    suspend operator fun invoke(id: Int) {
        repository.popTimer(id)
    }
}
