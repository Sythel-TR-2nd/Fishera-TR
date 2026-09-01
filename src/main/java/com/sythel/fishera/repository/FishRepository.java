package com.sythel.fishera.repository;

import com.sythel.fishera.database.DatabaseManager;
import com.sythel.fishera.fish.CaughtFish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FishRepository {

    private final DatabaseManager databaseManager;

    public FishRepository(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public void save(CaughtFish fish,
                     String uuid,
                     String playerName) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        try (PreparedStatement statement =
                     connection.prepareStatement("""
                INSERT INTO caught_fish
                (uuid, player_name, fish_id, rarity, weight, price, caught_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """)) {

            statement.setString(
                    1,
                    uuid
            );

            statement.setString(
                    2,
                    playerName
            );

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

    public List<EventCatchResult> getHeaviestCatches(
            long startTime,
            long endTime) {

        Connection connection =
                databaseManager.getConnection();

        List<EventCatchResult> results =
                new ArrayList<>();

        if (connection == null) {
            return results;
        }

        try (PreparedStatement statement =
                     connection.prepareStatement("""
                SELECT uuid, player_name, weight
                FROM caught_fish
                WHERE caught_at >= ?
                  AND caught_at <= ?
                  AND weight = (
                      SELECT MAX(cf2.weight)
                      FROM caught_fish cf2
                      WHERE cf2.uuid = caught_fish.uuid
                        AND cf2.caught_at >= ?
                        AND cf2.caught_at <= ?
                  )
                GROUP BY uuid, player_name, weight
                ORDER BY weight DESC
                LIMIT 3
                """)) {

            statement.setLong(
                    1,
                    startTime
            );

            statement.setLong(
                    2,
                    endTime
            );

            statement.setLong(
                    3,
                    startTime
            );

            statement.setLong(
                    4,
                    endTime
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    results.add(
                            new EventCatchResult(
                                    resultSet.getString(
                                            "uuid"
                                    ),
                                    resultSet.getString(
                                            "player_name"
                                    ),
                                    resultSet.getDouble(
                                            "weight"
                                    )
                            )
                    );

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return results;

    }

    public List<EventCatchCount> getCatchCounts(
            long startTime,
            long endTime) {

        Connection connection =
                databaseManager.getConnection();

        List<EventCatchCount> results =
                new ArrayList<>();

        if (connection == null) {
            return results;
        }

        try (PreparedStatement statement =
                     connection.prepareStatement("""
                SELECT uuid, player_name, COUNT(*) AS catch_count
                FROM caught_fish
                WHERE caught_at >= ?
                  AND caught_at <= ?
                GROUP BY uuid, player_name
                ORDER BY catch_count DESC
                """)) {

            statement.setLong(
                    1,
                    startTime
            );

            statement.setLong(
                    2,
                    endTime
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    results.add(
                            new EventCatchCount(
                                    resultSet.getString(
                                            "uuid"
                                    ),
                                    resultSet.getString(
                                            "player_name"
                                    ),
                                    resultSet.getInt(
                                            "catch_count"
                                    )
                            )
                    );

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return results;

    }

    public static class EventCatchResult {

        private final String uuid;

        private final String playerName;

        private final double weight;

        public EventCatchResult(
                String uuid,
                String playerName,
                double weight) {

            this.uuid = uuid;

            this.playerName = playerName;

            this.weight = weight;

        }

        public String getUuid() {

            return uuid;

        }

        public String getPlayerName() {

            return playerName;

        }

        public double getWeight() {

            return weight;

        }

    }

    public static class EventCatchCount {

        private final String uuid;

        private final String playerName;

        private final int count;

        public EventCatchCount(
                String uuid,
                String playerName,
                int count) {

            this.uuid = uuid;

            this.playerName = playerName;

            this.count = count;

        }

        public String getUuid() {

            return uuid;

        }

        public String getPlayerName() {

            return playerName;

        }

        public int getCount() {

            return count;

        }

    }

}
