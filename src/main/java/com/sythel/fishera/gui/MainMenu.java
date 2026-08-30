package com.sythel.fishera.gui;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.configuration.file.FileConfiguration;

public class MainMenu {

    private static final int SIZE = 27;

    private final MainMenuBuilder builder;
    private final ConfigManager configManager;

    public MainMenu(
            MainMenuBuilder builder,
            ConfigManager configManager) {

        this.builder = builder;
        this.configManager = configManager;

    }

    public void open(Player player) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.main.title",
                        "Fishera Balıkçılık"
                );

        title =
                ColorUtil.color(title);

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        SIZE,
                        title
                );

        builder.build(inventory);

        player.openInventory(inventory);

    }

}