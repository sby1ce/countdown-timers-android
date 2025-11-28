package com.example.countdowntimers

import android.app.Application
import android.content.Context
import com.example.countdowntimers.model.AppDataContainer
import com.example.countdowntimers.lib.Clock
import com.example.countdowntimers.lib.SystemClock
import com.example.countdowntimers.lib.TimerRepository
import com.example.countdowntimers.model.ServerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.create
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
    fun providesTimerRepository(@ApplicationContext context: Context, server: ServerService): TimerRepository =
        AppDataContainer(context, server).timerRepository

    @Provides
    fun providesRetrofit(): ServerService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:7070")
            .build()
        return retrofit.create<ServerService>()
    }
}
