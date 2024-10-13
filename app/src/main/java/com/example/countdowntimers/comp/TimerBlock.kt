package com.example.countdowntimers.comp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countdowntimers.lib.ITimer
import com.example.countdowntimers.lib.TimerViewModel

private fun timerProps(
    timers: List<ITimer>,
    renders: List<List<String>>,
): List<TimerProps> {
    return (timers zip renders).mapIndexed { id, (timer, origin) ->
        TimerProps(id, timer.name, origin)
    }
}

@Composable
fun TimerBlock(viewModel: TimerViewModel) {
    val state = timerProps(viewModel.timers.toList(), viewModel.renders)

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
    TimerBlock(TimerViewModel())
}
