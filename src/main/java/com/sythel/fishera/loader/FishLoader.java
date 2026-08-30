package com.sythel.fishera.loader;

import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.registry.FishRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FishLoader {

    private final JavaPlugin plugin;
    private final FishRegistry fishRegistry;

    public FishLoader(JavaPlugin plugin,
                      FishRegistry fishRegistry) {

        this.plugin = plugin;
        this.fishRegistry = fishRegistry;

    }

    public void load(FileConfiguration config) {

        fishRegistry.clear();

        ConfigurationSection section = config.getConfigurationSection("");

        if (section == null) {

            plugin.getLogger().warning("fish.yml boş!");

            return;

        }

        for (String id : section.getKeys(false)) {

            String rarity = config.getString(id + ".rarity", "common");
            int chance = config.getInt(id + ".chance", 100);
            double minWeight = config.getDouble(id + ".min-weight");
            double maxWeight = config.getDouble(id + ".max-weight");
            double basePrice = config.getDouble(id + ".base-price");
            String material = config.getString(id + ".material", "COD");
            int customModelData = config.getInt(id + ".custom-model-data");

            if (rarity.isBlank()) {

                plugin.getLogger().warning(
                        "Balık '" + id + "' için rarity boş."
                );

                continue;

            }

            if (chance < 0) {

                plugin.getLogger().warning(
                        "Balık '" + id + "' için chance negatif."
                );

                continue;

            }

            if (minWeight > maxWeight) {

                plugin.getLogger().warning(
                        "Balık '" + id + "' için min-weight max-weight'ten büyük."
                );

                continue;

            }

            if (material.isBlank()) {

                plugin.getLogger().warning(
                        "Balık '" + id + "' için material boş."
                );

                continue;

            }

            FishData fish = new FishData(
                    id,
                    config.getString(id + ".name", id),
                    rarity,
                    chance,
                    minWeight,
                    maxWeight,
                    basePrice,
                    material,
                    customModelData,
                    config.getStringList(id + ".lore"),
                    config.getStringList(id + ".hint")
            );

            fishRegistry.register(fish);

        }

        plugin.getLogger().info(
                fishRegistry.getAll().size() + " balık yüklendi."
        );

    }

}