package com.example.countdowntimers

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.model.TimerModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TimerModelUnitTest {
    private lateinit var model: TimerModel

    @Before
    fun setup() {
        model = TimerModel(listOf(Timer("1", "test", 0L)))
    }

    @Test
    fun testPresence() {
        assert(model.hasName("test"))
    }

    @Test
    fun testAddingTimer() {
        val new = model.addTimer("test1", 0, 0, 0)
        assert(new.hasName("test1"))
    }

    @Test
    fun testRemovingTimer() {
        val new = model.popTimer(0)
        assert(!new.hasName("test"))
        val empty = new.popTimer(0)
        assertEquals(new, empty)
    }

    @Test
    fun testRendering() {
        val renders = model.render(1)
        assertEquals(1, renders.size)
        assertEquals(1, renders[0].size)
        assert(renders[0][0].startsWith('-'))
    }

    @Test
    fun testNameIterating() {
        val names = model.names()
        assertEquals(listOf("test"), names)
    }
}
