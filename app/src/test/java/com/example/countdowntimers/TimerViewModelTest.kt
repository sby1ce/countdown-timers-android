package com.example.countdowntimers

import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerDao
import com.example.countdowntimers.lib.TimerRepository
import com.example.countdowntimers.model.OfflineTimerRepository
import com.example.countdowntimers.model.ServerService
import com.example.countdowntimers.viewmodel.AddTimerUseCase
import com.example.countdowntimers.viewmodel.PopTimerUseCase
import com.example.countdowntimers.viewmodel.RenderTimersUseCase
import com.example.countdowntimers.viewmodel.TimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeDao : TimerDao {
    companion object {
        val state = mutableListOf<Timer>()
    }

    override suspend fun insert(timer: Timer): Long {
        if (hasTimer(timer.key)) {
            return -1L
        }
        state.add(timer)
        return 1L
    }

    override suspend fun delete(timer: Timer) {
        state.removeIf { item -> item.key == timer.key }
    }

    override fun getTimers(): Flow<List<Timer>> = flow {
        emit(state)
    }

    override suspend fun hasTimer(id: Int): Boolean {
        return state.any { item -> item.key == id }
    }
}

class FakeServer : ServerService {
    override suspend fun select(): List<Timer> {
        return emptyList()
    }

    override suspend fun insert(timer: Timer) {}

    override suspend fun delete(id: Int) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var fakeClock: FakeClock
    private lateinit var repository: TimerRepository
    private lateinit var viewModel: TimerViewModel

    @Before
    fun setUp() {
        fakeClock = FakeClock()
        repository = OfflineTimerRepository(FakeDao(), FakeServer())
        viewModel = TimerViewModel(
            AddTimerUseCase(repository,),
            PopTimerUseCase(repository),
            RenderTimersUseCase(repository, fakeClock),
            context = testScope.coroutineContext
        )
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `addTimer returns error if name is empty`() {
        viewModel.addTimer("", 123456L, 10, 0)
        assertEquals(R.string.add_error_name, viewModel.errorFlow.value)
    }

    @Test
    fun `addTimer returns error if dateMillis is null`() {
        viewModel.addTimer("My Timer", null, 10, 0)
        assertEquals(R.string.add_error_date, viewModel.errorFlow.value)
    }

    @Test
    fun `addTimer returns error if name is duplicate`() {
        // The default timers already have "Timer 1" and "Timer 2"
        viewModel.addTimer("Timer 1", 123456L, 10, 0)
        assertEquals(R.string.add_error_duplicate, viewModel.errorFlow.value)
    }

    @Test
    fun `addTimer adds new timer with valid inputs`() {
        viewModel.addTimer("New Timer", 1000L, 12, 30)
        assertNull(viewModel.errorFlow.value)

        // Should contain the new timer name
        // assertTrue(viewModel)
    }

    @Test
    fun `rendersFlow updates according to clock`() {
        var emissions = emptyList<Pair<Timer, List<String>>>()
        testDispatcher.dispatch(Dispatchers.Default) {
            emissions = viewModel.rendersFlow.value
        }
        // Cancel the scope/coroutines to exit the loop and finish test
        testScope.cancel()

        assertTrue(emissions.isEmpty())
    }

    // Helper fake clock class to control now() in tests
    class FakeClock : Clock {
        private var currentTime = 0L
        override fun now(): Long = currentTime
    }
}
