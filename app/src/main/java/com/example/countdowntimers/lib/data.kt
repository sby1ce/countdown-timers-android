package com.example.countdowntimers.lib

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

@Dao
interface TimerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(timer: Timer)

    @Delete
    suspend fun delete(timer: Timer)

    @Query("SELECT * FROM timers")
    fun getTimers(): Flow<List<Timer>>

    @Query("SELECT EXISTS (SELECT \"key\" FROM timers WHERE \"key\" = :key)")
    suspend fun hasTimer(key: String): Boolean
}

@Database(entities = [Timer::class], version = 1, exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao

    companion object {
        @Volatile
        private var Instance: TimerDatabase? = null

        fun getDatabase(context: Context): TimerDatabase = Instance ?: synchronized(this) {
            Room.databaseBuilder(context, TimerDatabase::class.java, "timer_database")
                .fallbackToDestructiveMigration(false)
                .build()
        }
    }
}

interface TimerRepository {
    fun getTimers(): Flow<List<Timer>>

    suspend fun hasTimer(key: String): Boolean

    suspend fun insertTimer(timer: Timer)

    suspend fun deleteTimer(timer: Timer)
}

class OfflineTimerRepository(private val timerDao: TimerDao) : TimerRepository {
    override fun getTimers(): Flow<List<Timer>> = timerDao.getTimers()

    override suspend fun hasTimer(key: String): Boolean = timerDao.hasTimer(key)

    override suspend fun insertTimer(timer: Timer) = timerDao.insert(timer)

    override suspend fun deleteTimer(timer: Timer) = timerDao.delete(timer)
}

interface AppContainer {
    val timerRepository: TimerRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val timerRepository: TimerRepository by lazy {
        OfflineTimerRepository(TimerDatabase.getDatabase(context).timerDao())
    }
}
