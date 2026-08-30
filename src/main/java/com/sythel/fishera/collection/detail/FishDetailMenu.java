package com.sythel.fishera.collection.detail;

import com.sythel.fishera.collection.CollectionEntry;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FishDetailMenu {

    private static final int INVENTORY_SIZE = 36;

    private final FishDetailItemBuilder itemBuilder;

    private final ConfigManager configManager;

    public FishDetailMenu(
            FishDetailItemBuilder itemBuilder,
            ConfigManager configManager) {

        this.itemBuilder = itemBuilder;

        this.configManager = configManager;

    }

    public void open(
            Player player,
            CollectionEntry entry) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.fish-detail.title",
                        "Fishera • Balık Detayları"
                );

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        INVENTORY_SIZE,
                        ColorUtil.color(title)
                );

        itemBuilder.build(
                inventory,
                entry
        );

        player.openInventory(
                inventory
        );

    }

}