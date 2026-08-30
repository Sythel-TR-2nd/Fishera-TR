package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.registry.RarityRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.loader.BaitLoader;
import com.sythel.fishera.loader.FishLoader;
import com.sythel.fishera.loader.RarityLoader;
import com.sythel.fishera.loader.RodLoader;
import com.sythel.fishera.loader.TaskLoader;
import com.sythel.fishera.task.TaskData;

import java.util.List;

public class RegistrySetup {

    private final Fishera plugin;
    private final ConfigManager configManager;

    private FishRegistry fishRegistry;
    private RarityRegistry rarityRegistry;
    private RodRegistry rodRegistry;
    private BaitRegistry baitRegistry;

    private FishLoader fishLoader;
    private RarityLoader rarityLoader;
    private RodLoader rodLoader;
    private BaitLoader baitLoader;

    private TaskLoader taskLoader;
    private List<TaskData> tasks;

    public RegistrySetup(
            Fishera plugin,
            ConfigManager configManager) {

        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void initialize() {

        fishRegistry =
                new FishRegistry();

        rarityRegistry =
                new RarityRegistry();

        rodRegistry =
                new RodRegistry();

        baitRegistry =
                new BaitRegistry();

        fishLoader =
                new FishLoader(
                        plugin,
                        fishRegistry
                );

        rarityLoader =
                new RarityLoader(
                        plugin,
                        rarityRegistry
                );

        rodLoader =
                new RodLoader(
                        plugin,
                        rodRegistry
                );

        baitLoader =
                new BaitLoader();

        fishLoader.load(
                configManager.getFishConfig()
        );

        rarityLoader.load(
                configManager.getRarityConfig()
        );

        rodLoader.load(
                configManager.getRodConfig()
        );

        for (var bait : baitLoader.load(
                configManager.getBaitConfig()
        )) {

            baitRegistry.register(
                    bait
            );
        }

        taskLoader =
                new TaskLoader();

        tasks =
                taskLoader.load(
                        configManager.getTaskConfig()
                );
    }

    public FishRegistry getFishRegistry() {
        return fishRegistry;
    }

    public RarityRegistry getRarityRegistry() {
        return rarityRegistry;
    }

    public RodRegistry getRodRegistry() {
        return rodRegistry;
    }

    public BaitRegistry getBaitRegistry() {
        return baitRegistry;
    }

    public FishLoader getFishLoader() {
        return fishLoader;
    }

    public RarityLoader getRarityLoader() {
        return rarityLoader;
    }

    public RodLoader getRodLoader() {
        return rodLoader;
    }

    public BaitLoader getBaitLoader() {
        return baitLoader;
    }

    public TaskLoader getTaskLoader() {
        return taskLoader;
    }

    public List<TaskData> getTasks() {
        return tasks;
    }
}