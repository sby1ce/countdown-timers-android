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
                origin INTEGER NOT NULL
            )
        """
        )
    }

    fun create(timer: Timer): Timer {
        connection.prepareStatement("INSERT INTO timers (key, name, origin) VALUES (?, ?, ?)")
            .use { stmt ->
                stmt.setInt(1, timer.key)
                stmt.setString(2, timer.name)
                stmt.setLong(3, timer.origin)
                stmt.executeUpdate()
                return timer
            }
    }

    fun delete(id: Int): Boolean {
        return connection.prepareStatement("DELETE FROM timers WHERE key = ?")
            .use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate() > 0
            }
    }

    fun findAll(): List<Timer> {
        return connection.createStatement()
            .executeQuery("SELECT key, name, origin FROM timers")
            .use { rs ->
                generateSequence {
                    if (rs.next()) {
                        Timer(
                            rs.getInt("key"),
                            rs.getString("name"),
                            rs.getLong("origin")
                        )
                    } else null
                }.toList()
            }
    }
}
