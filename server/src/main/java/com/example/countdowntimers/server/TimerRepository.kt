package com.example.countdowntimers.server

import java.sql.DriverManager

class TimerRepository {
    private val connection =
        DriverManager.getConnection("jdbc:sqlite:timers.db")

    init {
        connection.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS timers (
                key INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                origin INTEGER NOT NULL,
                userId TEXT NOT NULL
            )
        """
        )
    }

    fun create(timer: TimerDto): Timer {
        connection.prepareStatement("INSERT INTO timers (key, name, origin, userId) VALUES (?, ?, ?, ?)")
            .use { stmt ->
                stmt.setInt(1, timer.key)
                stmt.setString(2, timer.name)
                stmt.setLong(3, timer.origin)
                stmt.setString(4, timer.userId)
                stmt.executeUpdate()
                return timer.into()
            }
    }

    fun delete(userId: String, id: Int): Boolean {
        return connection.prepareStatement("DELETE FROM timers WHERE userId = ? AND key = ?")
            .use { stmt ->
                stmt.setString(1, userId)
                stmt.setInt(2, id)
                stmt.executeUpdate() > 0
            }
    }

    fun findAll(userId: String): List<Timer> {
        return connection.prepareStatement("SELECT key, name, origin FROM timers WHERE userId = ?")
            .use { stmt ->
                stmt.setString(1, userId)
                stmt.executeQuery().use { rs ->
                    generateSequence {
                        if (rs.next()) {
                            Timer(
                                rs.getInt("key"),
                                rs.getString("name"),
                                rs.getLong("origin"),
                            )
                        } else null
                    }.toList()
                }
            }
    }
}
