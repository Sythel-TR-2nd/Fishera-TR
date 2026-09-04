package com.sythel.fishera.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin;

    private File fishFile;
    private FileConfiguration fishConfig;

    private File rarityFile;
    private FileConfiguration rarityConfig;

    private File rodFile;
    private FileConfiguration rodConfig;

    private File taskFile;
    private FileConfiguration taskConfig;

    private File baitFile;
    private FileConfiguration baitConfig;

    private File messagesFile;
    private FileConfiguration messagesConfig;

    private File guiFile;
    private FileConfiguration guiConfig;

    private File eventFile;
    private FileConfiguration eventConfig;

    private File simulationFile;
    private FileConfiguration simulationConfig;

    public ConfigManager(JavaPlugin plugin) {

        this.plugin = plugin;

        plugin.saveDefaultConfig();

        loadFishConfig();
        loadRarityConfig();
        loadRodConfig();
        loadTaskConfig();
        loadBaitConfig();
        loadMessagesConfig();
        loadGuiConfig();
        loadEventConfig();
        loadSimulationConfig();

    }

    private void loadFishConfig() {

        fishFile =
                new File(
                        plugin.getDataFolder(),
                        "fish.yml"
                );

        if (!fishFile.exists()) {

            plugin.saveResource(
                    "fish.yml",
                    false
            );

        }

        fishConfig =
                YamlConfiguration.loadConfiguration(
                        fishFile
                );

    }

    private void loadRarityConfig() {

        rarityFile =
                new File(
                        plugin.getDataFolder(),
                        "rarities.yml"
                );

        if (!rarityFile.exists()) {

            plugin.saveResource(
                    "rarities.yml",
                    false
            );

        }

        rarityConfig =
                YamlConfiguration.loadConfiguration(
                        rarityFile
                );

    }

    private void loadRodConfig() {

        rodFile =
                new File(
                        plugin.getDataFolder(),
                        "rods.yml"
                );

        if (!rodFile.exists()) {

            plugin.saveResource(
                    "rods.yml",
                    false
            );

        }

        rodConfig =
                YamlConfiguration.loadConfiguration(
                        rodFile
                );

    }

    private void loadTaskConfig() {

        taskFile =
                new File(
                        plugin.getDataFolder(),
                        "tasks.yml"
                );

        if (!taskFile.exists()) {

            plugin.saveResource(
                    "tasks.yml",
                    false
            );

        }

        taskConfig =
                YamlConfiguration.loadConfiguration(
                        taskFile
                );

    }

    private void loadBaitConfig() {

        baitFile =
                new File(
                        plugin.getDataFolder(),
                        "baits.yml"
                );

        if (!baitFile.exists()) {

            plugin.saveResource(
                    "baits.yml",
                    false
            );

        }

        baitConfig =
                YamlConfiguration.loadConfiguration(
                        baitFile
                );

    }

    private void loadMessagesConfig() {

        messagesFile =
                new File(
                        plugin.getDataFolder(),
                        "messages.yml"
                );

        if (!messagesFile.exists()) {

            plugin.saveResource(
                    "messages.yml",
                    false
            );

        }

        messagesConfig =
                YamlConfiguration.loadConfiguration(
                        messagesFile
                );

    }

    private void loadGuiConfig() {

        guiFile =
                new File(
                        plugin.getDataFolder(),
                        "gui.yml"
                );

        if (!guiFile.exists()) {

            plugin.saveResource(
                    "gui.yml",
                    false
            );

        }

        guiConfig =
                YamlConfiguration.loadConfiguration(
                        guiFile
                );

    }

    private void loadEventConfig() {

        eventFile =
                new File(
                        plugin.getDataFolder(),
                        "event.yml"
                );

        if (!eventFile.exists()) {

            plugin.saveResource(
                    "event.yml",
                    false
            );

        }

        eventConfig =
                YamlConfiguration.loadConfiguration(
                        eventFile
                );

    }

    private void loadSimulationConfig() {

        simulationFile =
                new File(
                        plugin.getDataFolder(),
                        "simulation.yml"
                );

        if (!simulationFile.exists()) {

            plugin.saveResource(
                    "simulation.yml",
                    false
            );

        }

        simulationConfig =
                YamlConfiguration.loadConfiguration(
                        simulationFile
                );

    }

    public List<String> getEnabledWorlds() {

        return plugin.getConfig()
                .getStringList(
                        "settings.enabled-worlds"
                );

    }

    public FileConfiguration getFishConfig() {

        return fishConfig;

    }

    public FileConfiguration getRarityConfig() {

        return rarityConfig;

    }

    public FileConfiguration getRodConfig() {

        return rodConfig;

    }

    public FileConfiguration getTaskConfig() {

        return taskConfig;

    }

    public FileConfiguration getBaitConfig() {

        return baitConfig;

    }

    public FileConfiguration getMessagesConfig() {

        return messagesConfig;

    }

    public FileConfiguration getGuiConfig() {

        return guiConfig;

    }

    public FileConfiguration getEventConfig() {

        return eventConfig;

    }

    public FileConfiguration getSimulationConfig() {

        return simulationConfig;

    }

    public void reload() {

        plugin.reloadConfig();

        loadFishConfig();
        loadRarityConfig();
        loadRodConfig();
        loadTaskConfig();
        loadBaitConfig();
        loadMessagesConfig();
        loadGuiConfig();
        loadEventConfig();
        loadSimulationConfig();

    }

}
