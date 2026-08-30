package com.sythel.fishera.gui.bait;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BaitMenu {

    private static final int INVENTORY_SIZE = 54;

    private final BaitMenuBuilder builder;
    private final ConfigManager configManager;

    public BaitMenu(
            BaitMenuBuilder builder,
            ConfigManager configManager) {

        this.builder = builder;
        this.configManager = configManager;

    }

    public void open(Player player) {

        open(
                player,
                0
        );

    }

    public void open(
            Player player,
            int page) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.bait.title",
                        "Fishera • Yemler"
                );

        title =
                ColorUtil.color(title);

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        INVENTORY_SIZE,
                        title
                );

        builder.build(
                player,
                inventory,
                page
        );

        player.openInventory(
                inventory
        );

    }

}