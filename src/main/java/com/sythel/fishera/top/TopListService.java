package com.sythel.fishera.top;

import com.sythel.fishera.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopListService {

    private final DatabaseManager databaseManager;

    public TopListService(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public List<TopEntry> getTopMoney() {

        return load("""
                SELECT player_name,
                       SUM(price) AS value
                FROM caught_fish
                GROUP BY uuid
                ORDER BY value DESC
                LIMIT 10
                """, "⛃");

    }

    public List<TopEntry> getTopFishCount() {

        return load("""
                SELECT player_name,
                       COUNT(*) AS value
                FROM caught_fish
                GROUP BY uuid
                ORDER BY value DESC
                LIMIT 10
                """, "Balık");

    }

    public List<TopEntry> getTopWeight() {

        return load("""
                SELECT player_name,
                       MAX(weight) AS value
                FROM caught_fish
                GROUP BY uuid
                ORDER BY value DESC
                LIMIT 10
                """, "KG");

    }

    public List<TopEntry> getTopCollection() {

        return load("""
                SELECT player_name,
                       COUNT(DISTINCT fish_id) AS value
                FROM caught_fish
                GROUP BY uuid
                ORDER BY value DESC
                LIMIT 10
                """, "Tür");

    }

    private List<TopEntry> load(String sql,
                                String suffix) {

        List<TopEntry> list =
                new ArrayList<>();

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return list;
        }

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                list.add(
                        new TopEntry(
                                result.getString("player_name"),
                                result.getDouble("value"),
                                suffix
                        )
                );

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return list;

    }

}