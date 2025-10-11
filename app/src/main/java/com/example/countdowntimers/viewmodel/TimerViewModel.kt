package com.example.countdowntimers.viewmodel

import androidx.annotation.StringRes
import com.example.countdowntimers.R
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.SystemClock
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.model.TimerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TimerViewModel(
    context: CoroutineContext = Dispatchers.Main,
    clock: Clock = SystemClock(),
) {
    private val _timersFlow = MutableStateFlow(
        TimerModel(
            listOf(
                Timer(key = "timer1", name = "Timer 1", origin = 0),
                Timer(key = "timer2", name = "Timer 2", origin = 10000),
            )
        )
    )
    val timersFlow: StateFlow<TimerModel> = _timersFlow.asStateFlow()

    private val _rendersFlow = MutableStateFlow<List<List<String>>>(emptyList())
    val rendersFlow: StateFlow<List<List<String>>> = _rendersFlow.asStateFlow()

    private val scope = CoroutineScope(context + SupervisorJob())

    // Start a coroutine to update the renders periodically
    init {
        scope.launch {
            // ticker API is obsolete, just use while true
            while (isActive) {
                val now = clock.now()
                _rendersFlow.value = _timersFlow.value.render(now)
                delay(1000)
            }
        }
    }

    @StringRes
    fun addTimer(
        name: String, dateMillis: Long?, hour: Int, minute: Int,
    ): Int? {
        if (name.isEmpty()) {
            return R.string.add_error_name
        } else if (dateMillis == null) {
            return R.string.add_error_date
        } else if (timersFlow.value.hasName(name)) {
            return R.string.add_error_duplicate
        }

        _timersFlow.value = timersFlow.value.addTimer(name, dateMillis, hour, minute)

        return null
    }

    fun popTimer(id: Int) {
        _timersFlow.value =
            timersFlow.value.popTimer(id)
    }
}
