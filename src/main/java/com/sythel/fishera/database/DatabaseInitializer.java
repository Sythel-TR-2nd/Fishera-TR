package com.sythel.fishera.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private final DatabaseManager databaseManager;

    public DatabaseInitializer(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public void initialize() {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS caught_fish (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        uuid TEXT NOT NULL,
                        player_name TEXT NOT NULL,
                        fish_id TEXT NOT NULL,
                        rarity TEXT NOT NULL,
                        weight REAL NOT NULL,
                        price REAL NOT NULL,
                        caught_at INTEGER NOT NULL
                    );
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS player_tasks (
                        uuid TEXT NOT NULL,
                        task_id TEXT NOT NULL,
                        progress REAL NOT NULL DEFAULT 0,
                        completed INTEGER NOT NULL DEFAULT 0,
                        claimed INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY (uuid, task_id)
                    );
                    """);

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

}