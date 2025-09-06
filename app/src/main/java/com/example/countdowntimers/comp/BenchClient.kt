/*
Copyright 2024 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

package com.example.countdowntimers.comp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.countdowntimers.ui.viewmodel.BenchViewModel

fun formatCompose(results: Long?): String {
    return if (results != null) "${
        results.toString().padStart(7)
    } microseconds average over 1000 runs" else "-"
}

@Composable
fun BenchClient(viewModel: BenchViewModel) {
    val results = viewModel.resultsFlow.collectAsState()

    Column {
        Text(text = "Kotlin")
        Text(text = formatCompose(results.value?.kt))

        Text(text = "Rust")
        Text(text = formatCompose(results.value?.rs))
    }

    Button(onClick = viewModel::bench) {
        Text(text = "Benchmark")
    }
}
