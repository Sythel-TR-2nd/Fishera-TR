package com.sythel.fishera.gui.task;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TaskMenu {

    private static final int INVENTORY_SIZE = 54;

    private final TaskMenuBuilder builder;

    private final TaskService taskService;

    private final ConfigManager configManager;

    public TaskMenu(
            TaskMenuBuilder builder,
            TaskService taskService,
            ConfigManager configManager) {

        this.builder = builder;

        this.taskService = taskService;

        this.configManager = configManager;

    }

    public void open(Player player) {

        open(
                player,
                0
        );

    }

    public void open(Player player,
                     int page) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.task.title",
                        "Fishera • Görevler"
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

    public TaskService getTaskService() {

        return taskService;

    }

}