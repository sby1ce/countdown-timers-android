package com.example.countdowntimers.viewmodel

import androidx.annotation.StringRes
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.countdowntimers.R
import com.example.countdowntimers.lib.ITimer
import com.example.countdowntimers.lib.getOrigin
import com.example.countdowntimers.lib.hashName
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel {
    private val _timersFlow = MutableStateFlow<List<ITimer>>(listOf(
        ITimer(key = "timer1", name = "Timer 1", origin = 0),
        ITimer(key = "timer2", name = "Timer 2", origin = 10000),
    ))
    val timersFlow: StateFlow<List<ITimer>> = _timersFlow.asStateFlow()

    private val _rendersFlow = MutableStateFlow<List<List<String>>>(emptyList())
    val rendersFlow: StateFlow<List<List<String>>> = _rendersFlow.asStateFlow()

    // Start a coroutine to update the renders periodically
    init {
       // _timersFlow.value =

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                _rendersFlow.value = ktTimers(origins(_timersFlow.value))
                delay(1000)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @StringRes
    fun addTimer(
        name: String, date: DatePickerState, time: TimePickerState,
    ): Int? {
        if (name.isEmpty()) {
            return R.string.add_error_name
        } else if (date.selectedDateMillis == null) {
            return R.string.add_error_date
        } else if (timersFlow.value.any { timer -> timer.name == name }) {
            return R.string.add_error_duplicate
        }

        _timersFlow.value = timersFlow.value + ITimer(
            key = hashName(name),
            name = name,
            origin = getOrigin(date, time)
        )

        return null
    }

    fun popTimer(id: Int) {
        _timersFlow.value =
            timersFlow.value.filterIndexed { index, _ -> index != id }
    }
}
