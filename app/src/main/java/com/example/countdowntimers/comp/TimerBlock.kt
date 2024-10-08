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
import com.example.countdowntimers.lib.TimerViewModel

@Composable
fun TimerBlock(viewModel: TimerViewModel) {
    val state = (viewModel.timers zip viewModel.renders)
        .mapIndexed { id, (timer, origin) ->
            TimerProps(id, timer.name, origin)
        }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for (timer in state)
                Timer(timer)
        }
        HorizontalDivider(thickness = 2.dp)
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "Create a timer by setting its name and datetime")

            AddTimer()

            Box {
                Button(onClick = {}) {
                    Text(text = "Switch")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerBlockPreview() {
    TimerBlock(TimerViewModel())
}
