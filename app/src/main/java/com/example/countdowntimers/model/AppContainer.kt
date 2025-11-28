package com.example.countdowntimers.model

import android.content.Context
import com.example.countdowntimers.lib.TimerDatabase
import com.example.countdowntimers.lib.TimerRepository

interface AppContainer {
    val timerRepository: TimerRepository
}

class AppDataContainer(context: Context, server: ServerService) :
    AppContainer {
    override val timerRepository: TimerRepository by lazy {
        OfflineTimerRepository(
            TimerDatabase.getDatabase(context).timerDao(),
            server
        )
    }
}
