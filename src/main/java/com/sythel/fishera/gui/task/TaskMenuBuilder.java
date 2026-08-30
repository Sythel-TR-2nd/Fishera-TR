package com.sythel.fishera.gui.task;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.task.TaskData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TaskMenuBuilder {

    private static final int TASKS_PER_PAGE = 45;

    private final TaskService taskService;

    private final ConfigManager configManager;

    public TaskMenuBuilder(
            TaskService taskService,
            ConfigManager configManager) {

        this.taskService = taskService;

        this.configManager = configManager;
    }

    public void build(
            Player player,
            Inventory inventory,
            int page) {

        fillBackground(inventory);

        List<TaskData> tasks =
                taskService.getTasks();

        int start =
                page * TASKS_PER_PAGE;

        int end =
                Math.min(
                        start + TASKS_PER_PAGE,
                        tasks.size()
                );

        for (int i = start; i < end; i++) {

            TaskData task =
                    tasks.get(i);

            inventory.setItem(
                    i - start,
                    createTaskItem(
                            player,
                            task,
                            i
                    )
            );
        }

        int maxPage =
                Math.max(
                        0,
                        (tasks.size() - 1)
                                / TASKS_PER_PAGE
                );

        if (page > 0) {

            inventory.setItem(
                    45,
                    createButton(
                            "previous-page"
                    )
            );
        }

        inventory.setItem(
                49,
                createButton(
                        "main-menu"
                )
        );

        if (page < maxPage) {

            inventory.setItem(
                    53,
                    createButton(
                            "next-page"
                    )
            );
        }
    }

    private ItemStack createTaskItem(
            Player player,
            TaskData task,
            int taskIndex) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.task";

        boolean completed =
                taskService.isTaskCompleted(
                        player,
                        task
                );

        boolean claimed =
                taskService.isTaskClaimed(
                        player,
                        task
                );

        TaskData activeTask =
                taskService.getActiveTask(
                        player
                );

        boolean active =
                activeTask != null
                        && activeTask.getId().equals(
                        task.getId()
                );

        boolean unlocked =
                isUnlocked(
                        player,
                        taskIndex
                );

        Material material;

        String status;

        if (claimed) {

            material =
                    getMaterial(
                            config,
                            path + ".statuses.claimed.material",
                            Material.LIME_DYE
                    );

            status =
                    config.getString(
                            path + ".statuses.claimed.text",
                            "&a✔ Tamamlandı"
                    );

        } else if (completed) {

            material =
                    getMaterial(
                            config,
                            path + ".statuses.completed.material",
                            Material.GOLD_INGOT
                    );

            status =
                    config.getString(
                            path + ".statuses.completed.text",
                            "&6★ Ödülünü Al!"
                    );

        } else if (active && unlocked) {

            material =
                    getMaterial(
                            config,
                            path + ".statuses.active.material",
                            Material.WRITABLE_BOOK
                    );

            status =
                    config.getString(
                            path + ".statuses.active.text",
                            "&e◆ Aktif Görev"
                    );

        } else {

            material =
                    getMaterial(
                            config,
                            path + ".statuses.locked.material",
                            Material.GRAY_DYE
                    );

            status =
                    config.getString(
                            path + ".statuses.locked.text",
                            "&8🔒 Kilitli"
                    );
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
                        task.getName()
                )
        );

        if (!unlocked) {

            List<String> lore =
                    getLore(
                            config,
                            path + ".locked-lore"
                    );

            List<String> replaced =
                    new ArrayList<>();

            for (String line : lore) {

                replaced.add(
                        line.replace(
                                "%status%",
                                status
                        )
                );
            }

            meta.setLore(
                    ColorUtil.color(
                            replaced
                    )
            );

        } else {

            double progress =
                    taskService.getProgress(
                            player,
                            task
                    );

            double target =
                    task.getAmount();

            double displayedProgress =
                    Math.min(
                            progress,
                            target
                    );


            List<String> lore =
                    getLore(
                            config,
                            path + ".task-lore"
                    );

            List<String> replaced =
                    new ArrayList<>();

            for (String line : lore) {

                replaced.add(
                        line
                                .replace(
                                        "%description%",
                                        task.getDescription() == null
                                                ? ""
                                                : task.getDescription()
                                )
                                .replace(
                                        "%progress%",
                                        formatNumber(
                                                displayedProgress
                                        )
                                )
                                .replace(
                                        "%target%",
                                        formatNumber(
                                                target
                                        )
                                )
                                .replace(
                                        "%reward%",
                                        String.format(
                                                "%,.2f",
                                                task.getReward()
                                        )
                                )
                                .replace(
                                        "%status%",
                                        status
                                )
                );
            }

            meta.setLore(
                    ColorUtil.color(
                            replaced
                    )
            );
        }

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;
    }

    private List<String> getLore(
            FileConfiguration config,
            String path) {

        List<String> lore =
                config.getStringList(
                        path
                );

        if (lore.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                lore
        );
    }

    private Material getMaterial(
            FileConfiguration config,
            String path,
            Material defaultMaterial) {

        String materialName =
                config.getString(
                        path,
                        defaultMaterial.name()
                );

        Material material =
                Material.matchMaterial(
                        materialName
                );

        if (material == null) {
            return defaultMaterial;
        }

        return material;
    }

    private boolean isUnlocked(
            Player player,
            int taskIndex) {

        if (taskIndex <= 0) {
            return true;
        }

        List<TaskData> tasks =
                taskService.getTasks();

        if (taskIndex >= tasks.size()) {
            return false;
        }

        TaskData previousTask =
                tasks.get(
                        taskIndex - 1
                );

        return taskService.isTaskClaimed(
                player,
                previousTask
        );
    }

    private String formatNumber(
            double value) {

        if (value == Math.floor(value)) {

            return String.format(
                    "%.0f",
                    value
            );
        }

        return String.format(
                "%.2f",
                value
        );
    }

    private ItemStack createButton(
            String id) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.task.buttons." + id;

        Material material =
                getMaterial(
                        config,
                        path + ".material",
                        Material.BARRIER
                );

        String name =
                config.getString(
                        path + ".name",
                        ""
                );

        List<String> lore =
                config.getStringList(
                        path + ".lore"
                );

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        name
                )
        );

        meta.setLore(
                ColorUtil.color(
                        lore
                )
        );

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;
    }

    private void fillBackground(
            Inventory inventory) {

        FileConfiguration config =
                configManager.getGuiConfig();

        Material material =
                getMaterial(
                        config,
                        "gui.task.background.material",
                        Material.BLACK_STAINED_GLASS_PANE
                );

        String name =
                config.getString(
                        "gui.task.background.name",
                        " "
                );

        ItemStack glass =
                new ItemStack(material);

        ItemMeta meta =
                glass.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            name
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
}