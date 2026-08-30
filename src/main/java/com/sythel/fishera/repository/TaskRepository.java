package com.sythel.fishera.repository;

import com.sythel.fishera.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRepository {

    private final DatabaseManager databaseManager;

    public TaskRepository(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public double getProgress(String uuid,
                              String taskId) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return 0;
        }

        String sql = """
                SELECT progress
                FROM player_tasks
                WHERE uuid = ?
                AND task_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, taskId);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    return result.getDouble(
                            "progress"
                    );

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return 0;

    }

    public boolean isCompleted(String uuid,
                               String taskId) {

        return getFlag(
                uuid,
                taskId,
                "completed"
        );

    }

    public boolean isClaimed(String uuid,
                             String taskId) {

        return getFlag(
                uuid,
                taskId,
                "claimed"
        );

    }

    public void setProgress(String uuid,
                            String taskId,
                            double progress) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        String sql = """
                INSERT INTO player_tasks
                    (uuid, task_id, progress)
                VALUES
                    (?, ?, ?)
                ON CONFLICT(uuid, task_id)
                DO UPDATE SET progress = excluded.progress
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, taskId);
            statement.setDouble(3, progress);

            statement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public void setCompleted(String uuid,
                             String taskId,
                             boolean completed) {

        setFlag(
                uuid,
                taskId,
                "completed",
                completed
        );

    }

    public void setClaimed(String uuid,
                           String taskId,
                           boolean claimed) {

        setFlag(
                uuid,
                taskId,
                "claimed",
                claimed
        );

    }

    public void resetTask(String uuid,
                          String taskId) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        String sql = """
                INSERT INTO player_tasks
                    (uuid, task_id, progress, completed, claimed)
                VALUES
                    (?, ?, 0, 0, 0)
                ON CONFLICT(uuid, task_id)
                DO UPDATE SET
                    progress = 0,
                    completed = 0,
                    claimed = 0
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, taskId);

            statement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    private boolean getFlag(String uuid,
                            String taskId,
                            String column) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return false;
        }

        String sql =
                "SELECT "
                        + column
                        + " FROM player_tasks "
                        + "WHERE uuid = ? "
                        + "AND task_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, taskId);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    return result.getInt(column) == 1;

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;

    }

    private void setFlag(String uuid,
                         String taskId,
                         String column,
                         boolean value) {

        Connection connection =
                databaseManager.getConnection();

        if (connection == null) {
            return;
        }

        String sql =
                """
                INSERT INTO player_tasks
                    (uuid, task_id, %s)
                VALUES
                    (?, ?, ?)
                ON CONFLICT(uuid, task_id)
                DO UPDATE SET %s = excluded.%s
                """.formatted(
                        column,
                        column,
                        column
                );

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, taskId);
            statement.setInt(
                    3,
                    value ? 1 : 0
            );

            statement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

}