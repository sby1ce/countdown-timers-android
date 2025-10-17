import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.countdowntimers.comp.Timer
import com.example.countdowntimers.comp.TimerProps
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun timer_displaysContent_andHandlesClick() {
        var popCalled = false

        val props = TimerProps(
            id = 1,
            name = "Test Timer",
            countdowns = listOf("10s", "20s", "30s"),
            pop = { popCalled = true }
        )

        composeTestRule.setContent {
            Timer(props)
        }

        composeTestRule.onNodeWithText("Test Timer").assertIsDisplayed()

        composeTestRule.onNodeWithText("10s").assertIsDisplayed()
        composeTestRule.onNodeWithText("20s").assertIsDisplayed()
        composeTestRule.onNodeWithText("30s").assertIsDisplayed()

        val deleteButton = composeTestRule.onNodeWithContentDescription("delete")
        deleteButton.assertIsDisplayed()
        deleteButton.performClick()

        assert(popCalled)
    }
}
