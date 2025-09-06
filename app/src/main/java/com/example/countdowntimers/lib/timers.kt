/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

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

data class Origins(
    val kt: List<Long>,
)

data class ITimer(
    val key: String,
    val name: String,
    val origin: Long,
)

private data class TimeUnit(
    val suffix: String,
    val divisor: Long,
)

const val W_DIVISOR: Long = 1000 * 60 * 60 * 24 * 7
const val D_DIVISOR: Long = 1000 * 60 * 60 * 24
const val H_DIVISOR: Long = 1000 * 60 * 60
const val M_DIVISOR: Long = 1000 * 60
const val S_DIVISOR: Long = 1000
const val MS_DIVISOR: Long = 1

private val timeUnits: List<TimeUnit> = listOf(
    TimeUnit(suffix = "w", divisor = W_DIVISOR),
    TimeUnit(suffix = "d", divisor = D_DIVISOR),
    TimeUnit(suffix = "h", divisor = H_DIVISOR),
    TimeUnit(suffix = "m", divisor = M_DIVISOR),
    TimeUnit(suffix = "s", divisor = S_DIVISOR),
    TimeUnit(suffix = "ms", divisor = MS_DIVISOR),
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

fun calculateInterval(interval: Long, divisor: Long): Pair<Long, String> {
    val newInterval: Long = interval % divisor
    val unitCount: Long = interval / divisor
    return newInterval to unitCount.toString()
}

private interface Accumulate<out A : Accumulate<A>> {
    fun accumulate(
        interval: Long,
        formatOption: FormatOption,
    ): Pair<Long, A>

    fun plus(element: String): A
}

private data class Accumulator(
    val inner: String,
) : Accumulate<Accumulator> {
    override fun accumulate(
        interval: Long,
        formatOption: FormatOption,
    ): Pair<Long, Accumulator> {
        val timeUnit: TimeUnit = formatOption.toTimeUnit()
        val (newInterval, unitCount) = calculateInterval(
            interval,
            timeUnit.divisor
        )
        val newAccumulator = Accumulator(
            inner = inner + unitCount + timeUnit.suffix + ' ',
        )
        return newInterval to newAccumulator
    }

    override fun plus(element: String): Accumulator {
        return Accumulator(
            inner = inner + element,
        )
    }
}

private fun next(formatOptions: List<FormatOption>): List<FormatOption> {
    return formatOptions.drop(1)
}

private fun <A : Accumulate<A>> reduceInterval(
    interval: Long,
    accumulator: A,
    formatOptions: List<FormatOption>,
): A {
    if (formatOptions.isEmpty()) {
        return accumulator
    }
    val formatOption = formatOptions[0]
    val (newInterval, newAccumulator) = accumulator.accumulate(
        interval,
        formatOption
    )
    return reduceInterval(
        newInterval, newAccumulator, next(formatOptions)
    )
}

private fun <A : Accumulate<A>> convert(
    interval: Long,
    accumulator: A,
    formatOptions: List<FormatOption>,
): A {
    val absInterval = abs(interval)
    val newAccumulator: A =
        if (interval < 0) accumulator.plus("-") else accumulator

    return reduceInterval(absInterval, newAccumulator, formatOptions)
}

private fun <A : Accumulate<A>> update(
    accumulators: List<A>,
    origin: Long,
    now: Long,
): List<A> {
    val interval: Long = origin - now
    val formatOptions: List<FormatOption> = listOf(
        FormatOption.Day,
        FormatOption.Hour,
        FormatOption.Minute,
        FormatOption.Second,
    )

    return accumulators.map { accumulator: A ->
        convert(
            interval,
            accumulator,
            formatOptions
        )
    }
}

fun ktTimers(origins: Origins): List<List<String>> {
    val now: Long = System.currentTimeMillis()
    val updateAccumulators: (Long) -> List<Accumulator> =
        { origin ->
            val accumulators: List<Accumulator> = listOf(Accumulator(String()))
            update(accumulators, origin, now)
        }

    return origins.kt.map(updateAccumulators).map { accumulators ->
        accumulators.map { acc: Accumulator -> acc.inner }
    }
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

fun origins(timers: List<ITimer>): Origins {
    return Origins(timers.map { timer -> timer.origin })
}
