package com.sythel.fishera.loader;

import com.sythel.fishera.task.TaskData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskLoader {

    public List<TaskData> load(FileConfiguration config) {

        List<TaskData> tasks =
                new ArrayList<>();

        ConfigurationSection section =
                config.getConfigurationSection("tasks");

        if (section == null) {
            return tasks;
        }

        for (String id : section.getKeys(false)) {

            String path =
                    "tasks." + id;

            String name =
                    config.getString(
                            path + ".name",
                            id
                    );

            String description =
                    config.getString(
                            path + ".description",
                            ""
                    );

            String type =
                    config.getString(
                            path + ".type",
                            "CATCH_FISH"
                    );

            String fishId =
                    config.getString(
                            path + ".fish",
                            null
                    );

            double amount =
                    config.getDouble(
                            path + ".amount",
                            1
                    );

            double reward =
                    config.getDouble(
                            path + ".reward",
                            0
                    );

            tasks.add(
                    new TaskData(
                            id,
                            name,
                            description,
                            type,
                            fishId,
                            amount,
                            reward
                    )
            );

        }

        return Collections.unmodifiableList(tasks);

    }

}