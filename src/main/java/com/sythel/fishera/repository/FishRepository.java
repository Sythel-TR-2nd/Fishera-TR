package com.sythel.fishera.repository;

import com.sythel.fishera.database.DatabaseManager;
import com.sythel.fishera.fish.CaughtFish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FishRepository {

    private final DatabaseManager databaseManager;

    public FishRepository(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public void save(CaughtFish fish,
                     String uuid,
                     String playerName) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO caught_fish
                (uuid, player_name, fish_id, rarity, weight, price, caught_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """)) {

            statement.setString(1, uuid);

            statement.setString(2, playerName);

            statement.setString(
                    3,
                    fish.getFishData().getId()
            );

            statement.setString(
                    4,
                    fish.getRarityData().getId()
            );

            statement.setDouble(
                    5,
                    fish.getWeight()
            );

            statement.setDouble(
                    6,
                    fish.getPrice()
            );

            statement.setLong(
                    7,
                    System.currentTimeMillis()
            );

            statement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

}