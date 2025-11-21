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
    suspend fun insert(timer: Timer): Long

    @Delete
    suspend fun delete(timer: Timer)

    @Query("SELECT * FROM timers")
    fun getTimers(): Flow<List<Timer>>

    @Query("SELECT EXISTS (SELECT \"key\" FROM timers WHERE \"key\" = :id)")
    suspend fun hasTimer(id: Int): Boolean
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

    suspend fun hasTimer(id: Int): Boolean

    suspend fun insertTimer(timer: Timer): Boolean

    suspend fun deleteTimer(timer: Timer)
}
