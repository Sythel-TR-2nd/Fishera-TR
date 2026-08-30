package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.builder.BaitItemBuilder;
import com.sythel.fishera.builder.RodItemBuilder;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.top.TopListService;
import com.sythel.fishera.world.WorldManager;

public class PluginInitializer {

    private final Fishera plugin;

    private DatabaseSetup databaseSetup;

    public PluginInitializer(
            Fishera plugin) {

        this.plugin = plugin;

    }

    public void initialize() {

        ConfigManager configManager =
                new ConfigManager(
                        plugin
                );

        WorldManager worldManager =
                new WorldManager(
                        configManager
                );

        MessageService messageService =
                new MessageService(
                        configManager
                );

        databaseSetup =
                new DatabaseSetup(
                        plugin
                );

        databaseSetup.initialize();

        RepositorySetup repositorySetup =
                new RepositorySetup(
                        databaseSetup.getDatabaseManager()
                );

        repositorySetup.initialize();

        RegistrySetup registrySetup =
                new RegistrySetup(
                        plugin,
                        configManager
                );

        registrySetup.initialize();

        ServiceSetup serviceSetup =
                new ServiceSetup(
                        plugin,
                        registrySetup.getFishRegistry(),
                        registrySetup.getRarityRegistry(),
                        registrySetup.getRodRegistry(),
                        registrySetup.getBaitRegistry(),
                        repositorySetup.getFishRepository(),
                        repositorySetup.getCollectionRepository(),
                        repositorySetup.getTaskRepository(),
                        registrySetup.getTasks(),
                        messageService
                );

        serviceSetup.initialize();

        TopListService topListService =
                new TopListService(
                        databaseSetup.getDatabaseManager()
                );

        BaitItemBuilder baitItemBuilder =
                new BaitItemBuilder(
                        plugin
                );

        RodItemBuilder rodItemBuilder =
                new RodItemBuilder(
                        plugin
                );

        MenuSetup menuSetup =
                new MenuSetup(
                        configManager,
                        registrySetup.getBaitRegistry(),
                        registrySetup.getRodRegistry(),
                        baitItemBuilder,
                        rodItemBuilder,
                        serviceSetup.getCollectionService(),
                        serviceSetup.getSellService(),
                        serviceSetup.getEconomyService(),
                        serviceSetup.getTaskService()
                );

        menuSetup.initialize();

        FishingSetup fishingSetup =
                new FishingSetup(
                        plugin,
                        registrySetup.getRodRegistry(),
                        registrySetup.getBaitRegistry(),
                        serviceSetup.getRewardService(),
                        worldManager,
                        messageService
                );

        fishingSetup.initialize();

        ListenerSetup listenerSetup =
                new ListenerSetup(
                        plugin
                );

        listenerSetup.initialize(
                fishingSetup.getFishingManager(),
                fishingSetup.getMinigameService(),
                registrySetup.getBaitRegistry(),
                registrySetup.getRodRegistry(),
                baitItemBuilder,
                rodItemBuilder,
                serviceSetup.getEconomyService(),
                messageService,
                serviceSetup.getSellService(),
                menuSetup.getCollectionMenu(),
                menuSetup.getDetailMenu(),
                menuSetup.getMainMenu(),
                menuSetup.getBaitMenu(),
                menuSetup.getRodMenu(),
                menuSetup.getSellMenu(),
                menuSetup.getTopListMenu(),
                menuSetup.getTaskMenu(),
                topListService,
                serviceSetup.getTaskService(),
                configManager
        );

        CommandSetup commandSetup =
                new CommandSetup(
                        plugin
                );

        commandSetup.initialize(
                menuSetup.getMainMenu(),
                menuSetup.getCollectionMenu(),
                configManager,
                registrySetup.getFishRegistry(),
                registrySetup.getRarityRegistry(),
                registrySetup.getRodRegistry(),
                registrySetup.getBaitRegistry(),
                registrySetup.getFishLoader(),
                registrySetup.getRarityLoader(),
                registrySetup.getRodLoader(),
                registrySetup.getBaitLoader(),
                registrySetup.getTaskLoader(),
                serviceSetup.getTaskService()
        );
    }

    public void shutdown() {

        if (databaseSetup != null) {

            databaseSetup.shutdown();

        }

    }

}