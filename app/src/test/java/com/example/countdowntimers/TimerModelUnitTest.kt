package com.example.countdowntimers

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.model.TimerRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TimerRepositoryTest {

    private lateinit var repository: TimerRepository
    private val initialTimers = listOf(
        Timer(key = 1, name = "timer1", origin = 1000L)
    )

    @Before
    fun setup() {
        repository = TimerRepository(initialTimers)
    }

    @Test
    fun addTimer_success_addsTimer() = runTest {
        val success = repository.addTimer("newTimer", 0L, 0, 0)
        assertTrue(success)

        val timers = repository.getTimers()
        assertTrue(timers.any { it.name == "newTimer" })
    }

    @Test
    fun popTimer_removesTimer() = runTest {
        repository.popTimer(1)
        val timers = repository.getTimers()
        assertFalse(timers.any { it.key == 1 })
    }

    @Test
    fun addTimer_duplicateName_fails() = runTest {
        val result = repository.addTimer("timer1", 0L, 0, 0)
        assertFalse(result)
    }
}

