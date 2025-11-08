import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.countdowntimers.comp.AddTimer
import com.example.countdowntimers.model.TimerRepository
import com.example.countdowntimers.viewmodel.TimerViewModel
import org.junit.Rule
import org.junit.Test

class AddTimerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addTimer_uiTest() {
        val viewModel = TimerViewModel(TimerRepository(emptyList()), clock = TimerBlockTest.FakeClock())

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
