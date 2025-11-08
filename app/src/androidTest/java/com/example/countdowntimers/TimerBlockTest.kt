import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.countdowntimers.comp.TimerBlock
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.model.TimerRepository
import com.example.countdowntimers.viewmodel.TimerViewModel
import org.junit.Rule
import org.junit.Test

class TimerBlockTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun timerBlock_uiElementsDisplayed() {
        val viewModel = TimerViewModel(TimerRepository(emptyList()), clock = FakeClock())

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

        // Interact with the Switch button (no action needed, just click)
        composeTestRule.onNodeWithText("Switch")
            .performClick()

        // If timers are present (modify ViewModel to add a timer before testing),
        // check if timer names are displayed
        // For example, simulate adding a timer:
        viewModel.addTimer("Test Timer", System.currentTimeMillis(), 10, 0)
        composeTestRule.waitForIdle()
        // Check for the timer name display:
        composeTestRule.onNodeWithText("Test Timer").assertIsDisplayed()
    }

    class FakeClock : Clock {
        private var currentTime = 0L
        override fun now(): Long = currentTime
    }
}
