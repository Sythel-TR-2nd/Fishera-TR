package com.sythel.fishera.collection.detail;

import com.sythel.fishera.collection.CollectionEntry;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.RarityUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FishDetailItemBuilder {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    private final ConfigManager configManager;

    public FishDetailItemBuilder(
            ConfigManager configManager) {

        this.configManager = configManager;

    }

    public void build(
            Inventory inventory,
            CollectionEntry entry) {

        fillBackground(inventory);

        inventory.setItem(
                13,
                createFish(entry)
        );

        inventory.setItem(
                11,
                createStatistics(entry)
        );

        inventory.setItem(
                15,
                createInformation(entry)
        );

        inventory.setItem(
                27,
                createBackButton()
        );

        inventory.setItem(
                35,
                createCloseButton()
        );

    }

    private void fillBackground(
            Inventory inventory) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.fish-detail.background";

        Material material =
                Material.matchMaterial(
                        config.getString(
                                path + ".material",
                                "BLACK_STAINED_GLASS_PANE"
                        )
                );

        if (material == null) {

            material =
                    Material.BLACK_STAINED_GLASS_PANE;

        }

        ItemStack glass =
                new ItemStack(material);

        ItemMeta meta =
                glass.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            config.getString(
                                    path + ".name",
                                    " "
                            )
                    )
            );

            glass.setItemMeta(meta);

        }

        for (int i = 0;
             i < inventory.getSize();
             i++) {

            inventory.setItem(
                    i,
                    glass
            );

        }

    }

    private ItemStack createFish(
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

        String path =
                "gui.fish-detail.fish";

        meta.setDisplayName(
                ColorUtil.color(
                        entry.getFishData().getName()
                )
        );

        if (entry.getFishData().getCustomModelData() > 0) {

            meta.setCustomModelData(
                    entry.getFishData().getCustomModelData()
            );

        }

        List<String> lore =
                new ArrayList<>();

        lore.add(
                RarityUtil.getDisplayName(
                        entry.getFishData().getRarity()
                )
        );

        lore.add("");

        lore.add(
                ColorUtil.color(
                        config.getString(
                                path + ".rarity-description-prefix",
                                "&7"
                        ) +
                                RarityUtil.getDescription(
                                        entry.getFishData().getRarity()
                                )
                )
        );

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createStatistics(
            CollectionEntry entry) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.fish-detail.statistics";

        Material material =
                Material.matchMaterial(
                        config.getString(
                                path + ".material",
                                "BOOK"
                        )
                );

        if (material == null) {

            material = Material.BOOK;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        meta.setDisplayName(
                ColorUtil.color(
                        config.getString(
                                path + ".name",
                                "&b📖 İstatistikler"
                        )
                )
        );

        List<String> lore =
                new ArrayList<>();

        addConfiguredLine(
                lore,
                config,
                path + ".total-caught-label",
                "&7Toplam Yakalanan"
        );

        addConfiguredLine(
                lore,
                config,
                path + ".total-caught-value",
                "&f%catch-count%",
                "%catch-count%",
                String.valueOf(entry.getCatchCount())
        );

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                path + ".heaviest-label",
                "&7En Ağır"
        );

        addConfiguredLine(
                lore,
                config,
                path + ".heaviest-value",
                "&f%best-weight% kg",
                "%best-weight%",
                String.format(
                        Locale.US,
                        "%.2f",
                        entry.getBestWeight()
                )
        );

        lore.add("");

        addConfiguredLine(
                lore,
                config,
                path + ".most-valuable-label",
                "&7En Değerli"
        );

        addConfiguredLine(
                lore,
                config,
                path + ".most-valuable-value",
                "&6%best-price% ⛃",
                "%best-price%",
                String.format(
                        Locale.US,
                        "%.2f",
                        entry.getBestPrice()
                )
        );

        if (entry.getFirstCatch() > 0) {

            lore.add("");

            addConfiguredLine(
                    lore,
                    config,
                    path + ".first-catch-label",
                    "&7İlk Yakalanma"
            );

            addConfiguredLine(
                    lore,
                    config,
                    path + ".first-catch-value",
                    "&f%first-catch%",
                    "%first-catch%",
                    DATE_FORMAT.format(
                            Instant.ofEpochMilli(
                                    entry.getFirstCatch()
                            )
                    )
            );

        }

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createInformation(
            CollectionEntry entry) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.fish-detail.information";

        Material material =
                Material.matchMaterial(
                        config.getString(
                                path + ".material",
                                "WRITABLE_BOOK"
                        )
                );

        if (material == null) {

            material = Material.WRITABLE_BOOK;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        meta.setDisplayName(
                ColorUtil.color(
                        config.getString(
                                path + ".name",
                                "&6💡 Balık Bilgisi"
                        )
                )
        );

        List<String> lore =
                new ArrayList<>();

        lore.add("");

        lore.add(
                RarityUtil.getDisplayName(
                        entry.getFishData().getRarity()
                )
        );

        lore.add("");

        String hintFormat =
                config.getString(
                        path + ".hint-format",
                        "&7• %hint%"
                );

        for (String hint :
                entry.getFishData().getHint()) {

            lore.add(
                    ColorUtil.color(
                            hintFormat.replace(
                                    "%hint%",
                                    hint
                            )
                    )
            );

        }

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createBackButton() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.fish-detail.buttons.back";

        Material material =
                Material.matchMaterial(
                        config.getString(
                                path + ".material",
                                "ARROW"
                        )
                );

        if (material == null) {

            material = Material.ARROW;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        meta.setDisplayName(
                ColorUtil.color(
                        config.getString(
                                path + ".name",
                                "&e◀ Geri"
                        )
                )
        );

        meta.setLore(
                ColorUtil.color(
                        config.getStringList(
                                path + ".lore"
                        )
                )
        );

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createCloseButton() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.fish-detail.buttons.close";

        Material material =
                Material.matchMaterial(
                        config.getString(
                                path + ".material",
                                "BARRIER"
                        )
                );

        if (material == null) {

            material = Material.BARRIER;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        meta.setDisplayName(
                ColorUtil.color(
                        config.getString(
                                path + ".name",
                                "&c✖ Kapat"
                        )
                )
        );

        meta.setLore(
                ColorUtil.color(
                        config.getStringList(
                                path + ".lore"
                        )
                )
        );

        item.setItemMeta(meta);

        return item;

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

        lore.add(
                ColorUtil.color(
                        config.getString(
                                path,
                                defaultValue
                        ).replace(
                                placeholder,
                                value
                        )
                )
        );

    }

}