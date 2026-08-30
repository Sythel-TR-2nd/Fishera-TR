package com.sythel.fishera.loader;

import com.sythel.fishera.bait.BaitData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaitLoader {

    public List<BaitData> load(FileConfiguration config) {

        List<BaitData> baits =
                new ArrayList<>();

        ConfigurationSection section =
                config.getConfigurationSection("baits");

        if (section == null) {
            return baits;
        }

        for (String id : section.getKeys(false)) {

            String path =
                    "baits." + id;

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

            String material =
                    config.getString(
                            path + ".material",
                            "WHEAT_SEEDS"
                    );

            int customModelData =
                    config.getInt(
                            path + ".custom-model-data",
                            0
                    );

            double price =
                    config.getDouble(
                            path + ".price",
                            0
                    );

            int weight =
                    config.getInt(
                            path + ".weight",
                            0
                    );

            int speed =
                    config.getInt(
                            path + ".speed",
                            0
                    );

            int luck =
                    config.getInt(
                            path + ".luck",
                            0
                    );

            int maxAmount =
                    config.getInt(
                            path + ".max-amount",
                            64
                    );

            baits.add(
                    new BaitData(
                            id,
                            name,
                            description,
                            material,
                            customModelData,
                            price,
                            weight,
                            speed,
                            luck,
                            maxAmount
                    )
            );

        }

        return Collections.unmodifiableList(
                baits
        );

    }

}