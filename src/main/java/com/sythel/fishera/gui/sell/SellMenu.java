package com.sythel.fishera.gui.sell;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SellMenu {

    private static final int SIZE = 27;

    private final SellMenuBuilder builder;
    private final ConfigManager configManager;

    public SellMenu(
            SellMenuBuilder builder,
            ConfigManager configManager) {

        this.builder = builder;
        this.configManager = configManager;

    }

    public void open(Player player) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.sell.title",
                        "Balık Pazarı"
                );

        title =
                ColorUtil.color(title);

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        SIZE,
                        title
                );

        builder.build(
                player,
                inventory
        );

        player.openInventory(
                inventory
        );

    }

    public String getTitle() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.sell.title",
                        "Balık Pazarı"
                );

        return ColorUtil.color(title);

    }

}