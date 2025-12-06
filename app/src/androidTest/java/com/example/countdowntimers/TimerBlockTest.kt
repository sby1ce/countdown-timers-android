package com.example.countdowntimers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.countdowntimers.comp.TimerBlock
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.model.OfflineTimerRepository
import com.example.countdowntimers.viewmodel.AddTimerUseCase
import com.example.countdowntimers.viewmodel.PopTimerUseCase
import com.example.countdowntimers.viewmodel.RenderTimersUseCase
import com.example.countdowntimers.viewmodel.TimerViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Rule
import org.junit.Test

class TimerBlockTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val composeTestRule = createComposeRule()

    @After
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun timerBlock_uiElementsDisplayed() {
        val repository = OfflineTimerRepository(AddTimerTest.FakeDao(), AddTimerTest.FakeServer())
        val viewModel = TimerViewModel(
            AddTimerUseCase(repository),
            PopTimerUseCase(repository),
            RenderTimersUseCase(repository, FakeClock()),
            context = testScope.coroutineContext,
        )

        composeTestRule.setContent {
            TimerBlock(viewModel)
        }

        // Assert static texts
        composeTestRule.onNodeWithText("Create a timer by setting its name and datetime")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Switch")
            .assertIsDisplayed()

        // Assert that AddTimer's field label is shown (indirect confirmation)
        composeTestRule.onNodeWithText("Timer name here")
            .assertIsDisplayed()

        // If timers are present (modify ViewModel to add a timer before testing),
        // check if timer names are displayed
        // For example, simulate adding a timer:
        viewModel.addTimer("Test Timer", System.currentTimeMillis(), 10, 0)
        testScope.cancel()
        composeTestRule.waitForIdle()
        assert(viewModel.rendersFlow.value.isNotEmpty())
        // Check for the timer name display:
        composeTestRule.onNodeWithText("Test Timer").assertIsDisplayed()
    }

    class FakeClock : Clock {
        private var currentTime = 0L
        override fun now(): Long = currentTime
    }
}
