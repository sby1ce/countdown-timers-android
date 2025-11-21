package com.example.countdowntimers

import android.app.Application
import android.content.Context
import com.example.countdowntimers.model.AppDataContainer
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.SystemClock
import com.example.countdowntimers.lib.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class TimerApplication : Application()

@Module
@InstallIn(ViewModelComponent::class)
object TimersModule {
    @Provides
    fun provideClock(): Clock = SystemClock()
    @Provides
    fun providesContext(): CoroutineContext = Dispatchers.Main

    @Provides
    fun providesTimerRepository(context: Context): TimerRepository =
        AppDataContainer(context).timerRepository
}
