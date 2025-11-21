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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class RenderTimersUseCase @Inject constructor(
    private val repository: TimerRepository,
    private val clock: Clock
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(scope: CoroutineScope): Flow<List<Pair<Timer, List<String>>>> {
        return repository.getTimers()  // Flow<List<Timer>>
            .flatMapLatest { timers ->
                // Emit updated render state every second
                flow {
                    while (scope.isActive) {
                        val now = clock.now()
                        val render = timers zip ktTimers(origins(timers), now)
                        emit(render)
                        delay(1000)
                    }
                }
            }
    }
}

