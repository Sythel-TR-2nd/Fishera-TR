package com.sythel.fishera.loader;

import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.rod.RodSeries;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RodLoader {

    private final JavaPlugin plugin;

    private final RodRegistry rodRegistry;

    public RodLoader(JavaPlugin plugin,
                     RodRegistry rodRegistry) {

        this.plugin = plugin;
        this.rodRegistry = rodRegistry;

    }

    public void load(FileConfiguration config) {

        ConfigurationSection section =
                config.getConfigurationSection("");

        if (section == null) {

            plugin.getLogger().warning(
                    "rods.yml boş!"
            );

            return;

        }

        for (String id : section.getKeys(false)) {

            String seriesName =
                    config.getString(
                            id + ".series",
                            "KIYI_MIRASI"
                    );

            RodSeries series;

            try {

                series =
                        RodSeries.valueOf(seriesName);

            } catch (IllegalArgumentException ex) {

                plugin.getLogger().warning(
                        id + " için geçersiz seri."
                );

                continue;

            }

            RodData rod =
                    new RodData(

                            id,

                            config.getString(
                                    id + ".name",
                                    id
                            ),

                            series,

                            config.getString(
                                    id + ".description",
                                    ""
                            ),

                            config.getString(
                                    id + ".material",
                                    "FISHING_ROD"
                            ),

                            config.getInt(
                                    id + ".custom-model-data"
                            ),

                            config.getDouble(
                                    id + ".price"
                            ),

                            config.getDouble(
                                    id + ".max-weight"
                            ),

                            config.getString(
                                    id + ".max-rarity",
                                    "COMMON"
                            ),

                            config.getInt(
                                    id + ".min-wait"
                            ),

                            config.getInt(
                                    id + ".max-wait"
                            ),

                            config.getInt(
                                    id + ".weight"
                            ),

                            config.getInt(
                                    id + ".speed"
                            ),

                            config.getInt(
                                    id + ".luck"
                            ),

                            config.getInt(
                                    id + ".sell"
                            ),

                            config.getDouble(
                                    id + ".perfect-chance"
                            ),

                            config.getString(
                                    id + ".passive.name",
                                    ""
                            ),

                            config.getString(
                                    id + ".passive.description",
                                    ""
                            ),

                            config.getStringList(
                                    id + ".lore"
                            )

                    );

            rodRegistry.register(
                    rod
            );

        }

        plugin.getLogger().info(
                rodRegistry.size() +
                        " olta yüklendi."
        );

    }

}