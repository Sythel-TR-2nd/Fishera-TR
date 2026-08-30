package com.sythel.fishera.collection;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.CollectionService;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectionStatisticsBuilder {

    private final ConfigManager configManager;

    private final CollectionService collectionService;

    public CollectionStatisticsBuilder(
            ConfigManager configManager,
            CollectionService collectionService) {

        this.configManager = configManager;

        this.collectionService = collectionService;

    }

    public ItemStack build(Player player) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.collection.statistics";

        String materialName =
                config.getString(
                        path + ".material",
                        "WRITTEN_BOOK"
                );

        Material material =
                Material.matchMaterial(
                        materialName
                );

        if (material == null) {

            material = Material.WRITTEN_BOOK;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        int total =
                collectionService.getTotalFish();

        int caught =
                collectionService.getCaughtFish(player);

        int missing =
                collectionService.getMissingFish(player);

        double percent =
                collectionService.getCompletion(player);

        String name =
                config.getString(
                        path + ".name",
                        "&#4FC3F7&l📖 Koleksiyon İlerlemesi"
                );

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        List<String> lore =
                new ArrayList<>();

        List<String> configuredLore =
                config.getStringList(
                        path + ".lore"
                );

        for (String line : configuredLore) {

            lore.add(
                    ColorUtil.color(
                            line
                                    .replace(
                                            "%total%",
                                            String.valueOf(total)
                                    )
                                    .replace(
                                            "%caught%",
                                            String.valueOf(caught)
                                    )
                                    .replace(
                                            "%missing%",
                                            String.valueOf(missing)
                                    )
                                    .replace(
                                            "%percent%",
                                            formatPercent(percent)
                                    )
                                    .replace(
                                            "%progress%",
                                            createProgressBar(percent)
                                    )
            ));

        }

        addRarity(
                lore,
                config,
                path,
                "common",
                player
        );

        addRarity(
                lore,
                config,
                path,
                "uncommon",
                player
        );

        addRarity(
                lore,
                config,
                path,
                "rare",
                player
        );

        addRarity(
                lore,
                config,
                path,
                "epic",
                player
        );

        addRarity(
                lore,
                config,
                path,
                "legendary",
                player
        );

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    private void addRarity(
            List<String> lore,
            FileConfiguration config,
            String path,
            String rarity,
            Player player) {

        int caught =
                collectionService.getCaughtByRarity(
                        player,
                        rarity
                );

        int total =
                collectionService.getTotalByRarity(
                        rarity
                );

        String rarityPath =
                path + ".rarities." + rarity;

        List<String> lines =
                config.getStringList(
                        rarityPath
                );

        for (String line : lines) {

            lore.add(
                    ColorUtil.color(
                            line
                                    .replace(
                                            "%caught%",
                                            String.valueOf(caught)
                                    )
                                    .replace(
                                            "%total%",
                                            String.valueOf(total)
                                    )
            ));

        }

    }

    private String createProgressBar(
            double percent) {

        int bars =
                configProgressBarSize();

        int filled =
                (int) Math.round(
                        (percent / 100D) * bars
                );

        filled =
                Math.max(
                        0,
                        Math.min(
                                bars,
                                filled
                        )
                );

        String filledCharacter =
                getProgressBarValue(
                        "filled-character",
                        "█"
                );

        String emptyCharacter =
                getProgressBarValue(
                        "empty-character",
                        "█"
                );

        String filledColor =
                getProgressBarValue(
                        "filled-color",
                        "&a"
                );

        String emptyColor =
                getProgressBarValue(
                        "empty-color",
                        "&7"
                );

        StringBuilder builder =
                new StringBuilder();

        builder.append(
                filledColor
        );

        for (int i = 0; i < filled; i++) {

            builder.append(
                    filledCharacter
            );

        }

        builder.append(
                emptyColor
        );

        for (int i = filled; i < bars; i++) {

            builder.append(
                    emptyCharacter
            );

        }

        return ColorUtil.color(
                builder.toString()
        );

    }

    private int configProgressBarSize() {

        FileConfiguration config =
                configManager.getGuiConfig();

        return config.getInt(
                "gui.collection.statistics.progress-bar.size",
                16
        );

    }

    private String getProgressBarValue(
            String key,
            String defaultValue) {

        FileConfiguration config =
                configManager.getGuiConfig();

        return config.getString(
                "gui.collection.statistics.progress-bar." + key,
                defaultValue
        );

    }

    private String formatPercent(
            double percent) {

        return String.format(
                Locale.US,
                "%.1f",
                percent
        );

    }

}