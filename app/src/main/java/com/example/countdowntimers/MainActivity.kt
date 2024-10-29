package com.example.countdowntimers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.countdowntimers.comp.BenchClient
import com.example.countdowntimers.comp.TimerBlock
import com.example.countdowntimers.lib.BenchViewModel
import com.example.countdowntimers.lib.TimerViewModel
import com.example.countdowntimers.ui.theme.CountdownTimersTheme

enum class Screens {
    Index,
    Bench,
}

@Composable
fun IndexPage() {
    Column {
        Text(
            text = "Watch countdowns",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
        )

        TimerBlock(TimerViewModel())
    }
}

@Composable
fun BenchPage() {
    val viewModel = BenchViewModel()

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Benchmarks")
        Text(text = "Average time over 1000 function runs in microseconds")
        Text(text = "Press the button to run the benchmark")
        BenchClient(viewModel)
    }
}

@Composable
fun TimersNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Index.name,
        modifier = modifier,
    ) {
        composable(Screens.Index.name) {
            IndexPage()
        }
        composable(Screens.Bench.name) {
            BenchPage()
        }
    }
}

fun createNavBar(navController: NavHostController): @Composable () -> Unit {
    val toIndex = { navController.navigate(Screens.Index.name) }
    val toBench = { navController.navigate(Screens.Bench.name) }
    val actions: @Composable RowScope.() -> Unit = {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = toIndex) { Text("Index") }
            Button(onClick = toBench) { Text("Bench") }
        }
    }
    return {
        BottomAppBar(actions = actions, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun Content() {
    CountdownTimersTheme {
        val navController = rememberNavController()
        val bottomBar = createNavBar(navController)
        Scaffold(
            bottomBar = bottomBar,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            TimersNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    Content()
}
