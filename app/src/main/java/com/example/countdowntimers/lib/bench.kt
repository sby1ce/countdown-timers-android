/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

package com.example.countdowntimers.lib

import android.util.Log
import uniffi.cd_android.updateTimers
import kotlin.time.measureTime

fun rsWrapper(
    updater: (Long, List<Long>) -> List<List<String>>,
): (Origins) -> List<List<String>> {
    return { origins: Origins ->
        val now: Long = System.currentTimeMillis()
        updater(now, origins.kt)
    }
}

val rsTimers: (Origins) -> List<List<String>> = rsWrapper(::updateTimers)

data class Results(
    val kt: Long,
    val rs: Long,
)

fun seed(): Origins {
    val kt: List<Long> = listOf(0, 1696174196000, 1607025600000)
    return Origins(kt)
}

fun bench1000(func: (Origins) -> List<List<String>>, data: Origins): Long {
    val microseconds = measureTime {
        (0..1000).forEach { _ ->
            val renders: List<List<String>> = func(data)
            if (
                !renders.all { row -> row.all { v -> v.isNotEmpty() } }
            ) {
                Log.d("bench", "Something went wrong when benching")
            }
        }
    }.inWholeMicroseconds

    return microseconds
}
