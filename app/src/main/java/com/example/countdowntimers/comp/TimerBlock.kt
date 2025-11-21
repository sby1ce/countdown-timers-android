/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

package com.example.countdowntimers.comp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.viewmodel.TimerViewModel

private fun timerProps(
    viewModel: TimerViewModel,
    renders: List<Pair<Timer, List<String>>>,
): List<TimerProps> {
    return renders.map { (timer, origin) ->
        TimerProps(timer.key, timer.name, origin) { viewModel.popTimer(timer) }
    }
}

@Composable
fun TimerBlock(viewModel: TimerViewModel) {
    val renders by viewModel.rendersFlow.collectAsState()
    val state = timerProps(viewModel, renders)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for (timer in state) Timer(timer)
        }

        HorizontalDivider(thickness = 2.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Create a timer by setting its name and datetime")
            AddTimer(viewModel)
        }

        HorizontalDivider(thickness = 2.dp)

        Box {
            Button(onClick = {}) {
                Text(text = "Switch")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerBlockPreview() {
    TimerBlock(hiltViewModel<TimerViewModel>())
}
