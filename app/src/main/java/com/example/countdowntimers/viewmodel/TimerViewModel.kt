package com.example.countdowntimers.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countdowntimers.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val addTimerUseCase: AddTimerUseCase,
    private val popTimerUseCase: PopTimerUseCase,
    private val renderTimersUseCase: RenderTimersUseCase,
    context: CoroutineContext = Dispatchers.Main,
) : ViewModel() {
    private val _rendersFlow = MutableStateFlow<List<Pair<String, List<String>>>>(emptyList())
    val rendersFlow: StateFlow<List<Pair<String, List<String>>>> = _rendersFlow.asStateFlow()

    @StringRes
    private val _errorFlow = MutableStateFlow<Int?>(null)

    @StringRes
    val errorFlow = _errorFlow.asStateFlow()

    private val scope = CoroutineScope(context + SupervisorJob())

    // Start a coroutine to update the renders periodically
    init {
        scope.launch {
            // ticker API is obsolete, just use while true
            while (isActive) {
                _rendersFlow.value = renderTimersUseCase()
                delay(1000)
            }
        }
    }

    fun addTimer(
        name: String, dateMillis: Long?, hour: Int, minute: Int,
    ) {
        if (name.isEmpty()) {
            _errorFlow.value = R.string.add_error_name
            return
        } else if (dateMillis == null) {
            _errorFlow.value = R.string.add_error_date
            return
        }

        viewModelScope.launch {
            _errorFlow.value = if (addTimerUseCase(name, dateMillis, hour, minute)) {
                null
            } else {
                R.string.add_error_duplicate
            }
        }
    }

    fun popTimer(id: Int) {
        viewModelScope.launch {
            popTimerUseCase(id)
        }
    }
}
