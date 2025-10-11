package com.example.countdowntimers

import com.example.countdowntimers.lib.Origins
import com.example.countdowntimers.lib.ktTimers
import org.junit.Assert.assertEquals
import org.junit.Test

class TimersTest {
    @Test
    fun `timers render`() {
        val origins = Origins(listOf(0))
        val now: Long = 0;
        val timers: List<List<String>> = ktTimers(origins, now)
        assert(timers.isNotEmpty())
        assert(timers[0].isNotEmpty())
        assertEquals(timers[0][0], "0d 0h 0m 0s ")
    }
}
