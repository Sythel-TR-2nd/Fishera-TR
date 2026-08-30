package com.sythel.fishera.collection;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.RarityUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CollectionItemBuilder {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    private final ConfigManager configManager;

    public CollectionItemBuilder(
            ConfigManager configManager) {

        this.configManager = configManager;

    }

    public ItemStack build(
            CollectionEntry entry) {

        FileConfiguration config =
                configManager.getGuiConfig();

        Material material =
                Material.matchMaterial(
                        entry.getFishData().getMaterial()
                );

        if (material == null) {

            material = Material.COD;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        if (entry.getFishData().getCustomModelData() > 0) {

            meta.setCustomModelData(
                    entry.getFishData().getCustomModelData()
            );

        }

        String displayName;

        if (entry.isCaught()) {

            displayName =
                    entry.getFishData().getName();

        } else {

            displayName =
                    config.getString(
                            "gui.collection.fish.unknown-name",
                            "&8???????"
                    );

        }

        meta.setDisplayName(
                ColorUtil.color(displayName)
        );

        meta.setLore(
                createLore(
                        entry,
                        config
                )
        );

        item.setItemMeta(meta);

        return item;

    }

    private List<String> createLore(
            CollectionEntry entry,
            FileConfiguration config) {

        List<String> lore =
                new ArrayList<>();

        lore.add(
                RarityUtil.getDisplayName(
                        entry.getFishData().getRarity()
                )
        );

        lore.add(
                ColorUtil.color(
                        config.getString(
                                "gui.collection.fish.rarity-description-prefix",
                                "&7"
                        )
                                +
                                RarityUtil.getDescription(
                                        entry.getFishData().getRarity()
                                )
                )
        );

        lore.add(
                ColorUtil.color(
                        config.getString(
                                "gui.collection.fish.separator",
                                "&8────────────────"
                        )
                )
        );

        if (entry.isCaught()) {

            addCaughtLore(
                    lore,
                    entry,
                    config
            );

        } else {

            addUnknownLore(
                    lore,
                    entry,
                    config
            );

        }

        return lore;

    }

    private void addCaughtLore(
            List<String> lore,
            CollectionEntry entry,
            FileConfiguration config) {

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.description",
                "&a✔ Bu balığı keşfettin."
        );

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.total-caught-label",
                "&7Toplam Yakalanan"
        );

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.total-caught-value",
                "&f%catch-count%",
                "%catch-count%",
                String.valueOf(
                        entry.getCatchCount()
                )
        );

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.heaviest-label",
                "&7En Ağır"
        );

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.heaviest-value",
                "&f%best-weight% kg",
                "%best-weight%",
                String.format(
                        "%.2f",
                        entry.getBestWeight()
                )
        );

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.most-valuable-label",
                "&7En Değerli"
        );

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.caught.most-valuable-value",
                "&6%best-price% ⛃",
                "%best-price%",
                String.format(
                        "%.2f",
                        entry.getBestPrice()
                )
        );

        if (entry.getFirstCatch() > 0) {

            lore.add("");

            addConfiguredLine(
                    lore,
                    config,
                    "gui.collection.fish.caught.first-catch-label",
                    "&7İlk Yakalanma"
            );

            addConfiguredLine(
                    lore,
                    config,
                    "gui.collection.fish.caught.first-catch-value",
                    "&f%first-catch%",
                    "%first-catch%",
                    DATE_FORMAT.format(
                            Instant.ofEpochMilli(
                                    entry.getFirstCatch()
                            )
                    )
            );

        }

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.details-button",
                "&e▶ Detayları görmek için tıkla."
        );

    }

    private void addUnknownLore(
            List<String> lore,
            CollectionEntry entry,
            FileConfiguration config) {

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.unknown.description",
                "&c🔒 Henüz keşfedilmedi."
        );

        lore.add("");

        if (!entry.getFishData().getHint().isEmpty()) {

            addConfiguredLine(
                    lore,
                    config,
                    "gui.collection.fish.unknown.hints-title",
                    "&6İpuçları"
            );

            for (String hint :
                    entry.getFishData().getHint()) {

                String hintLine =
                        config.getString(
                                "gui.collection.fish.unknown.hint-format",
                                "&7• %hint%"
                        );

                hintLine =
                        hintLine.replace(
                                "%hint%",
                                hint
                        );

                lore.add(
                        ColorUtil.color(
                                hintLine
                        )
                );

            }

        }

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                "gui.collection.fish.details-button",
                "&e▶ Detayları görmek için tıkla."
        );

    }

    private void addConfiguredLine(
            List<String> lore,
            FileConfiguration config,
            String path,
            String defaultValue) {

        lore.add(
                ColorUtil.color(
                        config.getString(
                                path,
                                defaultValue
                        )
                )
        );

    }

    private void addConfiguredLine(
            List<String> lore,
            FileConfiguration config,
            String path,
            String defaultValue,
            String placeholder,
            String value) {

        String line =
                config.getString(
                        path,
                        defaultValue
                );

        line =
                line.replace(
                        placeholder,
                        value
                );

        lore.add(
                ColorUtil.color(line)
        );

    }

}