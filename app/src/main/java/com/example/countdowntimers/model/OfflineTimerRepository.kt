package com.example.countdowntimers.model

import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.lib.TimerDao
import com.example.countdowntimers.lib.TimerRepository
import kotlinx.coroutines.flow.Flow

class OfflineTimerRepository(private val timerDao: TimerDao) : TimerRepository {
    override fun getTimers(): Flow<List<Timer>> = timerDao.getTimers()

    override suspend fun hasTimer(id: Int): Boolean = timerDao.hasTimer(id)

    // DAO returns -1 on error
    override suspend fun insertTimer(timer: Timer): Boolean =
        timerDao.insert(timer) != -1L

    override suspend fun deleteTimer(timer: Timer) = timerDao.delete(timer)
}
