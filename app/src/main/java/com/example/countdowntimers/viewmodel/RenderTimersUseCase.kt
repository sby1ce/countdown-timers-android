package com.example.countdowntimers.viewmodel

import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerRepository
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class RenderTimersUseCase @Inject constructor(
    private val repository: TimerRepository,
    private val clock: Clock
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(scope: CoroutineScope): Flow<List<Pair<Timer, List<String>>>> {
        val ticker = flow {
            while (scope.isActive) {
                emit(Unit)
                delay(1000)
            }
        }

        return repository.getTimers() // Flow<List<Timer>>
            .combine(ticker) { timers, _ ->
                val now = clock.now()
                timers zip ktTimers(origins(timers), now)
            }
    }
}
