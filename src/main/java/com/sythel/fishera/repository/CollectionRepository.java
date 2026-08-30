package com.sythel.fishera.repository;

import com.sythel.fishera.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CollectionRepository {

    private final DatabaseManager databaseManager;

    public CollectionRepository(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public boolean hasCaught(UUID uuid, String fishId) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                SELECT 1
                FROM caught_fish
                WHERE uuid = ?
                AND fish_id = ?
                LIMIT 1
                """)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, fishId);

            ResultSet result = statement.executeQuery();

            return result.next();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;

    }

    public int getCatchCount(UUID uuid, String fishId) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return 0;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                SELECT COUNT(*)
                FROM caught_fish
                WHERE uuid = ?
                AND fish_id = ?
                """)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, fishId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return 0;

    }

    public double getBestWeight(UUID uuid, String fishId) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return 0D;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                SELECT MAX(weight)
                FROM caught_fish
                WHERE uuid = ?
                AND fish_id = ?
                """)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, fishId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getDouble(1);
            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return 0D;

    }

    public double getBestPrice(UUID uuid, String fishId) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return 0D;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                SELECT MAX(price)
                FROM caught_fish
                WHERE uuid = ?
                AND fish_id = ?
                """)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, fishId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getDouble(1);
            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return 0D;

    }

    public long getFirstCatch(UUID uuid, String fishId) {

        Connection connection = databaseManager.getConnection();

        if (connection == null) {
            return 0L;
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                SELECT MIN(caught_at)
                FROM caught_fish
                WHERE uuid = ?
                AND fish_id = ?
                """)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, fishId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getLong(1);
            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return 0L;

    }

}