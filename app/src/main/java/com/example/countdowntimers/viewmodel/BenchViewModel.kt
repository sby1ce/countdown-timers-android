package com.example.countdowntimers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.Results
import com.example.countdowntimers.lib.bench1000
import com.example.countdowntimers.lib.ktTimers
import com.example.countdowntimers.lib.rsTimers
import com.example.countdowntimers.lib.seed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BenchViewModel @Inject constructor(private val clock: Clock) : ViewModel() {
    private val _resultsFlow = MutableStateFlow<Results?>(null)
    val resultsFlow = _resultsFlow.asStateFlow()

    fun bench() = viewModelScope.launch(Dispatchers.Default) {
        val origins = seed()

        val ktAvg = bench1000(func = ::ktTimers, data = origins, clock)
        val rsAvg = bench1000(func = rsTimers, data = origins, clock)

        _resultsFlow.value = Results(ktAvg, rsAvg)
    }
}
