package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.builder.BaitItemBuilder;
import com.sythel.fishera.builder.RodItemBuilder;
import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.collection.detail.FishDetailMenu;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.bait.BaitMenu;
import com.sythel.fishera.gui.rod.RodMenu;
import com.sythel.fishera.gui.sell.SellMenu;
import com.sythel.fishera.gui.task.TaskMenu;
import com.sythel.fishera.gui.top.TopListMenu;
import com.sythel.fishera.listeners.BaitListener;
import com.sythel.fishera.listeners.BaitMenuListener;
import com.sythel.fishera.listeners.CollectionListener;
import com.sythel.fishera.listeners.FishDetailListener;
import com.sythel.fishera.listeners.MainMenuListener;
import com.sythel.fishera.listeners.PlayerClickListener;
import com.sythel.fishera.listeners.PlayerFishListener;
import com.sythel.fishera.listeners.RodMenuListener;
import com.sythel.fishera.listeners.SellMenuListener;
import com.sythel.fishera.listeners.TaskListener;
import com.sythel.fishera.listeners.TopListListener;
import com.sythel.fishera.managers.FishingManager;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.service.EconomyService;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.MinigameService;
import com.sythel.fishera.service.SellService;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.top.TopListService;
import org.bukkit.Bukkit;

public class ListenerSetup {

    private final Fishera plugin;

    public ListenerSetup(
            Fishera plugin) {

        this.plugin = plugin;
    }

    public void initialize(
            FishingManager fishingManager,
            MinigameService minigameService,
            BaitRegistry baitRegistry,
            RodRegistry rodRegistry,
            BaitItemBuilder baitItemBuilder,
            RodItemBuilder rodItemBuilder,
            EconomyService economyService,
            MessageService messageService,
            SellService sellService,
            CollectionMenu collectionMenu,
            FishDetailMenu detailMenu,
            MainMenu mainMenu,
            BaitMenu baitMenu,
            RodMenu rodMenu,
            SellMenu sellMenu,
            TopListMenu topListMenu,
            TaskMenu taskMenu,
            TopListService topListService,
            TaskService taskService,
            ConfigManager configManager) {

        Bukkit.getPluginManager().registerEvents(
                new PlayerFishListener(
                        plugin,
                        fishingManager,
                        minigameService,
                        messageService
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new PlayerClickListener(
                        minigameService
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new BaitListener(
                        plugin,
                        baitRegistry,
                        messageService
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new MainMenuListener(
                        mainMenu,
                        collectionMenu,
                        sellMenu,
                        rodMenu,
                        topListMenu,
                        taskMenu,
                        baitMenu,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new BaitMenuListener(
                        baitRegistry,
                        baitItemBuilder,
                        economyService,
                        baitMenu,
                        mainMenu,
                        messageService,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new RodMenuListener(
                        rodRegistry,
                        rodItemBuilder,
                        economyService,
                        rodMenu,
                        mainMenu,
                        messageService,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new SellMenuListener(
                        sellService,
                        messageService,
                        sellMenu
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new CollectionListener(
                        collectionMenu,
                        detailMenu,
                        mainMenu,
                        messageService,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new FishDetailListener(
                        collectionMenu,
                        detailMenu,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new TopListListener(
                        topListMenu,
                        topListService,
                        mainMenu,
                        configManager
                ),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new TaskListener(
                        taskMenu,
                        taskService,
                        mainMenu,
                        messageService,
                        configManager
                ),
                plugin
        );
    }
}