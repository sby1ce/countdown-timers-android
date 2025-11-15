package com.example.countdowntimers
//
//import com.example.countdowntimers.R
//import com.example.countdowntimers.lib.Clock
//import com.example.countdowntimers.model.TimerRepository
//import com.example.countdowntimers.viewmodel.TimerViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.TestScope
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNull
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class TimerViewModelTest {
//
//    private val testDispatcher = StandardTestDispatcher()
//    private val testScope = TestScope(testDispatcher)
//
//    private lateinit var fakeClock: FakeClock
//    private lateinit var viewModel: TimerViewModel
//
//    @Before
//    fun setUp() {
//        fakeClock = FakeClock()
//        viewModel = TimerViewModel(TimerRepository(emptyList()), testDispatcher, fakeClock)
//    }
//
//    @After
//    fun tearDown() {
//        testScope.cancel()
//    }
//
//    @Test
//    fun `addTimer returns error if name is empty`() {
//        val error = viewModel.addTimer("", 123456L, 10, 0)
//        assertEquals(R.string.add_error_name, error)
//    }
//
//    @Test
//    fun `addTimer returns error if dateMillis is null`() {
//        val error = viewModel.addTimer("My Timer", null, 10, 0)
//        assertEquals(R.string.add_error_date, error)
//    }
//
//    @Test
//    fun `addTimer returns error if name is duplicate`() {
//        // The default timers already have "Timer 1" and "Timer 2"
//        val error = viewModel.addTimer("Timer 1", 123456L, 10, 0)
//        assertEquals(R.string.add_error_duplicate, error)
//    }
//
//    @Test
//    fun `addTimer adds new timer with valid inputs`() {
//        val error = viewModel.addTimer("New Timer", 1000L, 12, 30)
//        assertNull(error)
//
//        val timers = viewModel.timersFlow.value
//        // Should contain the new timer name
//        assertTrue(timers.hasName("New Timer"))
//    }
//
//    @Test
//    fun `rendersFlow updates according to clock`() {
//        var emissions = emptyList<List<String>>()
//        testDispatcher.dispatch(Dispatchers.Default) {
//            emissions = viewModel.rendersFlow.value
//        }
//        // Cancel the scope/coroutines to exit the loop and finish test
//        testScope.cancel()
//
//        assertTrue(emissions.isEmpty())
//    }
//
//    // Helper fake clock class to control now() in tests
//    class FakeClock : Clock {
//        private var currentTime = 0L
//        override fun now(): Long = currentTime
//    }
//}
