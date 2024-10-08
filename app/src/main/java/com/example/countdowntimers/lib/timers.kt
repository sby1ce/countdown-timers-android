package com.example.countdowntimers.lib

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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

class TimerViewModel : ViewModel() {
    var timers by mutableStateOf(
        listOf(
            ITimer(key = "timer1", name = "Timer 1", origin = 0),
            ITimer(key = "timer2", name = "Timer 2", origin = 10000),
        )
    )
        private set
    private val origins: List<Long>
        get() = timers.map { timer -> timer.origin }
    var renders by mutableStateOf<List<List<String>>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            while (true) {
                renders = ktTimers(origins)
                delay(timeMillis = 1000)
            }
        }
    }
}
