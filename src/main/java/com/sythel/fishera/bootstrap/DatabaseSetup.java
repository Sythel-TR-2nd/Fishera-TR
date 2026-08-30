package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.database.DatabaseInitializer;
import com.sythel.fishera.database.DatabaseManager;

public class DatabaseSetup {

    private final Fishera plugin;

    private DatabaseManager databaseManager;
    private DatabaseInitializer databaseInitializer;

    public DatabaseSetup(Fishera plugin) {

        this.plugin = plugin;

    }

    public void initialize() {

        databaseManager =
                new DatabaseManager(
                        plugin
                );

        databaseManager.connect();

        databaseInitializer =
                new DatabaseInitializer(
                        databaseManager
                );

        databaseInitializer.initialize();

    }

    public DatabaseManager getDatabaseManager() {

        return databaseManager;

    }

    public DatabaseInitializer getDatabaseInitializer() {

        return databaseInitializer;

    }

    public void shutdown() {

        if (databaseManager != null) {

            databaseManager.disconnect();

        }

    }

}