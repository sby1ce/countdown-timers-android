package com.example.countdowntimers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countdowntimers.comp.TimerBlock
import com.example.countdowntimers.lib.TimerViewModel
import com.example.countdowntimers.ui.theme.CountdownTimersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountdownTimersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Page(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Page(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Watch countdowns",
            modifier = Modifier.padding(8.dp)
        )

        TimerBlock(TimerViewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    CountdownTimersTheme {
        Page()
    }
}
