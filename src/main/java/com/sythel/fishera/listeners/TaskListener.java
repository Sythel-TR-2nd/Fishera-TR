package com.sythel.fishera.listeners;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.task.TaskMenu;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.task.TaskData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskListener implements Listener {

    private static final int TASKS_PER_PAGE = 45;

    private final TaskMenu taskMenu;
    private final TaskService taskService;
    private final MainMenu mainMenu;
    private final MessageService messageService;
    private final ConfigManager configManager;

    private final Map<UUID, Integer> pages =
            new HashMap<>();

    public TaskListener(
            TaskMenu taskMenu,
            TaskService taskService,
            MainMenu mainMenu,
            MessageService messageService,
            ConfigManager configManager) {

        this.taskMenu = taskMenu;
        this.taskService = taskService;
        this.mainMenu = mainMenu;
        this.messageService = messageService;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.task.title",
                        "Fishera • Görevler"
                );

        title =
                ColorUtil.color(title);

        if (!event.getView()
                .getTitle()
                .equals(title)) {

            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked()
                instanceof Player player)) {

            return;
        }

        if (event.getRawSlot() < 0
                || event.getRawSlot()
                >= event.getView()
                .getTopInventory()
                .getSize()) {

            return;
        }

        ItemStack clicked =
                event.getCurrentItem();

        if (clicked == null
                || clicked.getType() == Material.AIR) {

            return;
        }

        int slot =
                event.getRawSlot();

        int maxPage =
                Math.max(
                        0,
                        (taskService.getTasks().size() - 1)
                                / TASKS_PER_PAGE
                );

        switch (slot) {

            case 45 -> {

                int page =
                        Math.max(
                                pages.getOrDefault(
                                        player.getUniqueId(),
                                        0
                                ) - 1,
                                0
                        );

                pages.put(
                        player.getUniqueId(),
                        page
                );

                taskMenu.open(
                        player,
                        page
                );

                return;
            }

            case 49 -> {

                pages.remove(
                        player.getUniqueId()
                );

                mainMenu.open(
                        player
                );

                return;
            }

            case 53 -> {

                int page =
                        Math.min(
                                pages.getOrDefault(
                                        player.getUniqueId(),
                                        0
                                ) + 1,
                                maxPage
                        );

                pages.put(
                        player.getUniqueId(),
                        page
                );

                taskMenu.open(
                        player,
                        page
                );

                return;
            }

            default -> {
            }
        }

        if (slot < 0
                || slot >= TASKS_PER_PAGE) {

            return;
        }

        int page =
                pages.getOrDefault(
                        player.getUniqueId(),
                        0
                );

        int taskIndex =
                page * TASKS_PER_PAGE + slot;

        if (taskIndex >= taskService.getTasks().size()) {
            return;
        }

        TaskData task =
                taskService.getTasks().get(
                        taskIndex
                );

        if (!isUnlocked(
                player,
                taskIndex
        )) {

            player.sendMessage(
                    messageService.get(
                            "task.locked"
                    )
            );

            player.sendMessage(
                    messageService.get(
                            "task.previous-reward-required"
                    )
            );

            return;
        }

        TaskData activeTask =
                taskService.getActiveTask(
                        player
                );

        if (activeTask == null) {
            return;
        }

        if (!activeTask.getId().equals(
                task.getId()
        )) {

            return;
        }

        if (taskService.isTaskCompleted(
                player,
                task
        )) {

            boolean claimed =
                    taskService.claimReward(
                            player
                    );

            if (claimed) {

                taskMenu.open(
                        player,
                        page
                );
            }
        }
    }

    private boolean isUnlocked(
            Player player,
            int taskIndex) {

        if (taskIndex <= 0) {
            return true;
        }

        if (taskIndex >= taskService.getTasks().size()) {
            return false;
        }

        TaskData previousTask =
                taskService.getTasks().get(
                        taskIndex - 1
                );

        return taskService.isTaskClaimed(
                player,
                previousTask
        );
    }
}