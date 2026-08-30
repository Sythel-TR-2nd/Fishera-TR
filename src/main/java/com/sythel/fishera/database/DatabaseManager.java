package com.sythel.fishera.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final JavaPlugin plugin;

    private Connection connection;

    public DatabaseManager(JavaPlugin plugin) {

        this.plugin = plugin;

    }

    public void connect() {

        try {

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            File databaseFile = new File(
                    plugin.getDataFolder(),
                    "fishera.db"
            );

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + databaseFile.getAbsolutePath()
            );

            plugin.getLogger().info("SQLite veritabanına bağlanıldı.");

        } catch (SQLException exception) {

            plugin.getLogger().severe("SQLite bağlantısı kurulamadı!");
            exception.printStackTrace();

        }

    }

    public void disconnect() {

        if (connection == null) {
            return;
        }

        try {

            connection.close();

            plugin.getLogger().info("SQLite bağlantısı kapatıldı.");

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public Connection getConnection() {

        return connection;

    }

}