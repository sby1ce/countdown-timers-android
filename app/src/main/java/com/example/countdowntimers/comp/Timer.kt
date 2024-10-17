package com.example.countdowntimers.comp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countdowntimers.R

data class TimerProps(
    val id: Int,
    val name: String,
    val countdowns: List<String>,
    val pop: () -> Unit,
)

private val articleModifier = Modifier.defaultMinSize(300.dp)
private val h2Modifier = Modifier.padding(vertical = 16.dp)
private val pModifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp)

@Composable
fun Timer(props: TimerProps) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = articleModifier
        ) {
            Text(text = props.name, modifier = h2Modifier)

            Row {
                for (countdown in props.countdowns) {
                    Text(text = countdown, modifier = pModifier)
                }
            }
        }

        FloatingActionButton(
            onClick = props.pop,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.delete_16dp),
                contentDescription = "delete"
            )
        }
    }
}

@Preview
@Composable
fun TimerPreview() {
    Timer(TimerProps(0, "Timer 1", listOf("0s")) {})
}
