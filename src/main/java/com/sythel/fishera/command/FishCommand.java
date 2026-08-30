package com.sythel.fishera.command;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.loader.BaitLoader;
import com.sythel.fishera.loader.FishLoader;
import com.sythel.fishera.loader.RarityLoader;
import com.sythel.fishera.loader.RodLoader;
import com.sythel.fishera.loader.TaskLoader;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.registry.RarityRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.task.TaskData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishCommand implements CommandExecutor {

    private final Map<String, SubCommand> subCommands =
            new HashMap<>();

    private final MainMenu mainMenu;

    private final ConfigManager configManager;

    private final FishRegistry fishRegistry;

    private final RarityRegistry rarityRegistry;

    private final RodRegistry rodRegistry;

    private final BaitRegistry baitRegistry;

    private final FishLoader fishLoader;

    private final RarityLoader rarityLoader;

    private final RodLoader rodLoader;

    private final BaitLoader baitLoader;

    private final TaskLoader taskLoader;

    private final TaskService taskService;

    public FishCommand(
            MainMenu mainMenu,
            ConfigManager configManager,
            FishRegistry fishRegistry,
            RarityRegistry rarityRegistry,
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry,
            FishLoader fishLoader,
            RarityLoader rarityLoader,
            RodLoader rodLoader,
            BaitLoader baitLoader,
            TaskLoader taskLoader,
            TaskService taskService) {

        this.mainMenu = mainMenu;

        this.configManager = configManager;

        this.fishRegistry = fishRegistry;

        this.rarityRegistry = rarityRegistry;

        this.rodRegistry = rodRegistry;

        this.baitRegistry = baitRegistry;

        this.fishLoader = fishLoader;

        this.rarityLoader = rarityLoader;

        this.rodLoader = rodLoader;

        this.baitLoader = baitLoader;

        this.taskLoader = taskLoader;

        this.taskService = taskService;

    }

    public void register(
            SubCommand subCommand) {

        subCommands.put(
                subCommand.getName().toLowerCase(),
                subCommand
        );

    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args) {

        if (args.length == 0) {

            if (!(sender instanceof Player player)) {

                sender.sendMessage(
                        "Bu komut sadece oyuncular tarafından kullanılabilir."
                );

                return true;

            }

            mainMenu.open(player);

            return true;

        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission(
                    "fishera.reload")) {

                sender.sendMessage(
                        "§cBu komutu kullanmak için yetkin yok."
                );

                return true;

            }

            reload(sender);

            return true;

        }

        SubCommand subCommand =
                subCommands.get(
                        args[0].toLowerCase()
                );

        if (subCommand == null) {

            sender.sendMessage(
                    "§cBilinmeyen alt komut."
            );

            return true;

        }

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    "Bu komut sadece oyuncular tarafından kullanılabilir."
            );

            return true;

        }

        subCommand.execute(
                player,
                args
        );

        return true;

    }

    private void reload(
            CommandSender sender) {

        try {


            configManager.reload();

            fishRegistry.clear();

            rarityRegistry.clear();

            rodRegistry.clear();

            baitRegistry.clear();


            fishLoader.load(
                    configManager.getFishConfig()
            );

            rarityLoader.load(
                    configManager.getRarityConfig()
            );

            rodLoader.load(
                    configManager.getRodConfig()
            );

            List<BaitData> baits =
                    baitLoader.load(
                            configManager.getBaitConfig()
                    );

            for (BaitData bait : baits) {

                baitRegistry.register(
                        bait
                );

            }

            List<TaskData> tasks =
                    taskLoader.load(
                            configManager.getTaskConfig()
                    );

            taskService.reloadTasks(
                    tasks
            );

            sender.sendMessage("");

            sender.sendMessage(
                    "§a✔ Fishera Balıkçılık configleri yeniden yüklendi."
            );

            sender.sendMessage(
                    "§7Balık: §f"
                            + fishRegistry.getAll().size()
                            + " §8| §7Nadirlik: §f"
                            + rarityRegistry.getAll().size()
                            + " §8| §7Olta: §f"
                            + rodRegistry.getAll().size()
                            + " §8| §7Yem: §f"
                            + baitRegistry.getAll().size()
                            + " §8| §7Görev: §f"
                            + taskService.getTasks().size()
            );

            sender.sendMessage("");

        } catch (Exception exception) {

            sender.sendMessage("");

            sender.sendMessage(
                    "§c✘ Configler yüklenirken hata oluştu."
            );

            sender.sendMessage(
                    "§7Konsolu kontrol et."
            );

            sender.sendMessage("");

            exception.printStackTrace();

        }

    }

}