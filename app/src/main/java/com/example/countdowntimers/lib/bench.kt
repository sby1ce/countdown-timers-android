package com.example.countdowntimers.lib

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uniffi.cd_android.updateTimers
import kotlin.time.measureTime

fun rsWrapper(
    updater: (Long, List<Long>) -> List<List<String>>
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

private fun seed(): Origins {
    val kt: List<Long> = listOf(0, 1696174196000, 1607025600000)
    return Origins(kt)
}

fun bench1000(func: (Origins) -> List<List<String>>, data: Origins): Long {
    val microseconds = measureTime {
        for (i in 0..1000) {
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

class BenchViewModel {
    private val _resultsFlow = MutableStateFlow<Results?>(null)
    val resultsFlow = _resultsFlow.asStateFlow()

    fun bench() {
        val origins = seed()

        val ktAvg = bench1000(func = ::ktTimers, data = origins)
        val rsAvg = bench1000(func = rsTimers, data = origins)

        _resultsFlow.value = Results(ktAvg, rsAvg)
    }
}
