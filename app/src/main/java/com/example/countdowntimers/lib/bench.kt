/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

package com.example.countdowntimers.lib

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
//        for (i in 0..1000) {
//            val renders: List<List<String>> = func(data)
//            if (
//                !renders.all { row -> row.all { v -> v.isNotEmpty() } }
//            ) {
//                Log.d("bench", "Something went wrong when benching")
//            }
//        }

        (0..1000).asSequence()
            .map { _ -> func(data) }
            .filter { renders -> !renders.all { row -> row.all { v -> v.isNotEmpty() } } }
            .forEach { _ -> Log.d("bench", "Something went wrong when benching") }

    }.inWholeMicroseconds

    return microseconds
}
