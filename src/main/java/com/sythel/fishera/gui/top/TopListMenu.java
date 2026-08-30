package com.sythel.fishera.gui.top;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.top.TopCategory;
import com.sythel.fishera.top.TopEntry;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class TopListMenu {

    private static final int INVENTORY_SIZE = 54;

    private final TopListMenuBuilder builder;

    private final ConfigManager configManager;

    public TopListMenu(
            TopListMenuBuilder builder,
            ConfigManager configManager) {

        this.builder = builder;

        this.configManager = configManager;

    }

    public void open(
            Player player,
            TopCategory category,
            List<TopEntry> entries) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.top.title",
                        "Fishera • Top List"
                );

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        INVENTORY_SIZE,
                        ColorUtil.color(title)
                );

        builder.build(
                inventory,
                category,
                entries
        );

        player.openInventory(
                inventory
        );

    }

}