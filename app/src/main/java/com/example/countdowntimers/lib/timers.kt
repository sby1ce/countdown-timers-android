package com.example.countdowntimers.lib

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

data class ITimer(
    val key: String,
    val name: String,
    val origin: Long,
)

private enum class FormatOption {
    Week,
    Day,
    Hour,
    Minute,
    Second,
    Millisecond;

    fun toTimeUnit(): TimeUnit {
        return when (this) {
            Week -> timeUnits[0]
            Day -> timeUnits[1]
            Hour -> timeUnits[2]
            Minute -> timeUnits[3]
            Second -> timeUnits[4]
            Millisecond -> timeUnits[5]
        }
    }
}

private data class TimeUnit(
    val suffix: String,
    val divisor: Long,
)

private val timeUnits: List<TimeUnit> = listOf(
    TimeUnit(suffix = "w", divisor = 1000 * 60 * 60 * 24 * 7),
    TimeUnit(suffix = "d", divisor = 1000 * 60 * 60 * 24),
    TimeUnit(suffix = "h", divisor = 1000 * 60 * 60),
    TimeUnit(suffix = "m", divisor = 1000 * 60),
    TimeUnit(suffix = "s", divisor = 1000),
    TimeUnit(suffix = "ms", divisor = 1),
)

private fun reduceInterval(
    interval: Long,
    accumulator: String,
    formatOptions: List<FormatOption>,
): String {
    val formatLen = formatOptions.count()
    if (formatLen == 0) {
        return accumulator
    }
    val formatOption = formatOptions[0]
    val timeUnit = formatOption.toTimeUnit()
    val newInterval = interval % timeUnit.divisor
    val unitCount: Long = interval / timeUnit.divisor
    val newAccumulator =
        "$accumulator$unitCount${timeUnit.suffix} "
    return reduceInterval(
        newInterval, newAccumulator, formatOptions.drop(1)
    )
}

private fun convert(interval: Long, formatOptions: List<FormatOption>): String {
    val absInterval = abs(interval)
    val accumulator: String = if (interval >= 0) "" else "-"

    return reduceInterval(absInterval, accumulator, formatOptions)
}

private fun updateTimer(origin: Long, now: Long): List<String> {
    val interval: Long = origin - now
    val formatOptions: List<FormatOption> = listOf(
        FormatOption.Day,
        FormatOption.Hour,
        FormatOption.Minute,
        FormatOption.Second,
    )

    return listOf(convert(interval, formatOptions))
}

fun ktTimers(origins: List<Long>): List<List<String>> {
    val now: Long = System.currentTimeMillis()

    val result: List<List<String>> = origins.map { origin ->
        updateTimer(origin, now)
    }

    return result
}

fun hashName(timerName: String): String {
    val hash = timerName.fold(
        0,
    ) { hash, char -> 0 or (31 * hash + char.code) }
    return "timer${hash}"
}

@OptIn(ExperimentalMaterial3Api::class)
fun getOrigin(date: DatePickerState, time: TimePickerState): Long {
    return (date.selectedDateMillis
        ?: 0) + time.hour * 60 * 60 * 1000 + time.minute * 60 * 1000
}

private fun origins(timers: List<ITimer>): List<Long> {
    return timers.map { timer -> timer.origin }
}

class TimerViewModel {
    private val _timersFlow = MutableStateFlow<List<ITimer>>(emptyList())
    val timersFlow: StateFlow<List<ITimer>> = _timersFlow.asStateFlow()

    private val _rendersFlow = MutableStateFlow<List<List<String>>>(emptyList())
    val rendersFlow: StateFlow<List<List<String>>> = _rendersFlow.asStateFlow()

    // Start a coroutine to update the renders periodically
    init {
        _timersFlow.value = listOf(
            ITimer(key = "timer1", name = "Timer 1", origin = 0),
            ITimer(key = "timer2", name = "Timer 2", origin = 10000),
        )

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                _rendersFlow.value = ktTimers(origins(_timersFlow.value))
                delay(1000)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun addTimer(
        name: String, date: DatePickerState, time: TimePickerState,
    ): String? {
        if (name.isEmpty()) {
            return "Timer name should have name"
        } else if (date.selectedDateMillis == null) {
            return "Entered date is invalid"
        } else if (timersFlow.value.any { timer -> timer.name == name }) {
            return "Timer with the same name already exists"
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
