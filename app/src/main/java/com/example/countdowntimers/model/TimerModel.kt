package com.example.countdowntimers.model

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.hashName
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.origins
import kotlin.collections.plus

@OptIn(ExperimentalMaterial3Api::class)
private fun getOrigin(date: DatePickerState, time: TimePickerState): Long {
    return (date.selectedDateMillis
        ?: 0) + time.hour * 60 * 60 * 1000 + time.minute * 60 * 1000
}

class TimerModel(private val state: List<Timer>) {
    fun hasName(name: String): Boolean {
        return state.any { timer -> timer.name == name }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun addTimer(
        name: String, date: DatePickerState, time: TimePickerState,
    ): TimerModel {
        return TimerModel(state + Timer(
            key = hashName(name),
            name = name,
            origin = getOrigin(date, time)
        ))
    }

    fun popTimer(id: Int): TimerModel {
        return TimerModel(state.filterIndexed { index, _ -> index != id })
    }

    fun render(): List<List<String>> {
        return ktTimers(origins(state))
    }

    fun names(): List<String> {
        return state.map { timer -> timer.name }
    }
}
