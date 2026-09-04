package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.command.FishCommand;
import com.sythel.fishera.command.SimulationSubCommand;
import com.sythel.fishera.command.subcommand.CollectionSubCommand;
import com.sythel.fishera.collection.CollectionMenu;
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
import com.sythel.fishera.service.EventService;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.simulation.SimulationMenu;

public class CommandSetup {

    private final Fishera plugin;

    public CommandSetup(Fishera plugin) {
        this.plugin = plugin;
    }

    public void initialize(
            MainMenu mainMenu,
            CollectionMenu collectionMenu,
            SimulationMenu simulationMenu,
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
            TaskService taskService,
            EventService eventService) {

        FishCommand fishCommand =
                new FishCommand(
                        mainMenu,
                        configManager,
                        fishRegistry,
                        rarityRegistry,
                        rodRegistry,
                        baitRegistry,
                        fishLoader,
                        rarityLoader,
                        rodLoader,
                        baitLoader,
                        taskLoader,
                        taskService,
                        eventService
                );

        fishCommand.register(
                new CollectionSubCommand(
                        collectionMenu
                )
        );

        fishCommand.register(
                new SimulationSubCommand(
                        simulationMenu
                )
        );

        if (plugin.getCommand("fish") != null) {

            plugin.getCommand("fish")
                    .setExecutor(
                            fishCommand
                    );
        }
    }
}
