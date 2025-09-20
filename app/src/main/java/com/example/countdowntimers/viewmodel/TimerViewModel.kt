package com.example.countdowntimers.viewmodel

import androidx.annotation.StringRes
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.countdowntimers.R
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.model.TimerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel {
    private val _timersFlow = MutableStateFlow<TimerModel>(
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

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Start a coroutine to update the renders periodically
    init {
        scope.launch {
            // ticker API is obsolete, just use while true
            while (true) {
                _rendersFlow.value = _timersFlow.value.render()
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
        } else if (timersFlow.value.hasName(name)) {
            return R.string.add_error_duplicate
        }

        _timersFlow.value = timersFlow.value.addTimer(name, date, time)

        return null
    }

    fun popTimer(id: Int) {
        _timersFlow.value =
            timersFlow.value.popTimer(id)
    }
}
