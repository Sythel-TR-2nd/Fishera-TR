package com.sythel.fishera.loader;

import com.sythel.fishera.rarity.RarityData;
import com.sythel.fishera.registry.RarityRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RarityLoader {

    private final JavaPlugin plugin;
    private final RarityRegistry rarityRegistry;

    public RarityLoader(JavaPlugin plugin,
                        RarityRegistry rarityRegistry) {

        this.plugin = plugin;
        this.rarityRegistry = rarityRegistry;

    }

    public void load(FileConfiguration config) {

        rarityRegistry.clear();

        ConfigurationSection section = config.getConfigurationSection("");

        if (section == null) {

            plugin.getLogger().warning("rarities.yml boş!");

            return;

        }

        for (String id : section.getKeys(false)) {

            String name = config.getString(id + ".name", id);
            String color = config.getString(id + ".color", "#FFFFFF");

            if (name.isBlank()) {

                plugin.getLogger().warning(
                        "Rarity '" + id + "' için isim boş."
                );

                continue;

            }

            if (color.isBlank()) {

                plugin.getLogger().warning(
                        "Rarity '" + id + "' için renk boş."
                );

                continue;

            }

            RarityData rarity = new RarityData(
                    id,
                    name,
                    color,
                    config.getStringList(id + ".description")
            );

            rarityRegistry.register(rarity);

        }

        plugin.getLogger().info(
                rarityRegistry.getAll().size() + " rarity yüklendi."
        );

    }

}