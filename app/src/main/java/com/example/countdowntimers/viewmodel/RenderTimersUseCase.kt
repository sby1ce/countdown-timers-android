package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import com.example.countdowntimers.model.TimerRepository
import javax.inject.Inject

class RenderTimersUseCase @Inject constructor(
    private val repository: TimerRepository,
    private val clock: Clock
) {
    suspend operator fun invoke(): List<Pair<String, List<String>>> {
        val state = repository.getTimers()
        val now = clock.now()
        return state.map { it.name } zip ktTimers(origins(state), now)
    }
}
