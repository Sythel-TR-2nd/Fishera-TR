package com.sythel.fishera.gui.rod;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RodMenu {

    private static final int INVENTORY_SIZE = 36;

    private final RodMenuBuilder builder;
    private final ConfigManager configManager;

    public RodMenu(
            RodMenuBuilder builder,
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
                        "gui.rod.title",
                        "Fishera • Oltalar"
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

    public String getTitle() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.rod.title",
                        "Fishera • Oltalar"
                );

        return ColorUtil.color(title);

    }

}