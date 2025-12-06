package com.example.countdowntimers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.countdowntimers.comp.AddTimer
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerDao
import com.example.countdowntimers.model.OfflineTimerRepository
import com.example.countdowntimers.model.ServerService
import com.example.countdowntimers.viewmodel.AddTimerUseCase
import com.example.countdowntimers.viewmodel.PopTimerUseCase
import com.example.countdowntimers.viewmodel.RenderTimersUseCase
import com.example.countdowntimers.viewmodel.TimerViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class AddTimerTest {
    class FakeDao : TimerDao {
        companion object {
            val state = MutableStateFlow(mutableListOf<Timer>())
        }

        override suspend fun insert(timer: Timer): Long {
            if (hasTimer(timer.key)) {
                return -1L
            }
            state.value.add(timer)
            return 1L
        }

        override suspend fun delete(timer: Timer) {
            state.value.removeIf { item -> item.key == timer.key }
        }

        override fun getTimers(): Flow<List<Timer>> {
            return state.asStateFlow()
        }

        override suspend fun hasTimer(id: Int): Boolean {
            return state.value.any { item -> item.key == id }
        }
    }
    class FakeServer : ServerService {
        override suspend fun select(): List<Timer> {
            return emptyList()
        }

        override suspend fun insert(timer: Timer) {}

        override suspend fun delete(id: Int) {}
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addTimer_uiTest() {
        val repository = OfflineTimerRepository(FakeDao(), FakeServer())
        val viewModel = TimerViewModel(
            AddTimerUseCase(repository),
            PopTimerUseCase(repository),
            RenderTimersUseCase(repository, TimerBlockTest.FakeClock()),
        )

        composeTestRule.setContent {
            AddTimer(viewModel)
        }

        // Enter timer name
        composeTestRule.onNodeWithText("Timer name here").performTextInput("Test Timer")

        // Click Pick date button - open date picker dialog
        composeTestRule.onNodeWithText("Pick date").performClick()
        composeTestRule.onNodeWithText("Confirm").assertIsDisplayed()

        // Confirm date picker dialog
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithText("Confirm").assertDoesNotExist()

        // Click Pick time button - open time picker dialog
        composeTestRule.onNodeWithText("Pick time").performClick()
        composeTestRule.onNodeWithText("Confirm").assertIsDisplayed()

        // Confirm time picker dialog
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithText("Confirm").assertDoesNotExist()

        // Click Add timer button to submit
        composeTestRule.onNodeWithText("Add timer").performClick()

        composeTestRule.onNodeWithText("Timer name should have name").assertDoesNotExist()
        composeTestRule.onNodeWithText("Entered date is invalid").assertDoesNotExist()
        composeTestRule.onNodeWithText("Timer with the same name already exists")
            .assertDoesNotExist()
    }
}
