package com.example.countdowntimers

import android.app.Application
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.SystemClock
import com.example.countdowntimers.lib.Timer
import com.example.countdowntimers.model.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class TimerApplication : Application() {}

@Module
@InstallIn(ViewModelComponent::class)
object TimersModule {
    @Provides
    fun provideClock(): Clock = SystemClock()
    @Provides
    fun providesContext(): CoroutineContext = Dispatchers.Main

    @Provides
    fun providesRepository(): TimerRepository = TimerRepository(
        listOf(
            Timer(key = 1, name = "Timer 1", origin = 0),
            Timer(key = 2, name = "Timer 2", origin = 10000),
        )
    )
}
