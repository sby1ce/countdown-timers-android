package com.example.countdowntimers.comp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.countdowntimers.lib.BenchViewModel
import com.example.countdowntimers.lib.Results

fun formatKt(results: Results?): String {
    return if (results != null) "${
        results.kt.toString().padStart(7)
    } microseconds average over 1000 runs" else "-"
}

@Composable
fun BenchClient(viewModel: BenchViewModel) {
    val results = viewModel.resultsFlow.collectAsState()

    Column {
        Text(text = "Kotlin")
        Text(text = formatKt(results.value))
    }

    Button(onClick = viewModel::bench) {
        Text(text = "Benchmark")
    }
}
