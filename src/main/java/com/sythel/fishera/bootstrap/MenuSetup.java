package com.sythel.fishera.bootstrap;

import com.sythel.fishera.builder.BaitItemBuilder;
import com.sythel.fishera.builder.RodItemBuilder;
import com.sythel.fishera.collection.CollectionItemBuilder;
import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.collection.CollectionStatisticsBuilder;
import com.sythel.fishera.collection.detail.FishDetailItemBuilder;
import com.sythel.fishera.collection.detail.FishDetailMenu;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.MainMenuBuilder;
import com.sythel.fishera.gui.bait.BaitMenu;
import com.sythel.fishera.gui.bait.BaitMenuBuilder;
import com.sythel.fishera.gui.rod.RodMenu;
import com.sythel.fishera.gui.rod.RodMenuBuilder;
import com.sythel.fishera.gui.sell.SellMenu;
import com.sythel.fishera.gui.sell.SellMenuBuilder;
import com.sythel.fishera.gui.task.TaskMenu;
import com.sythel.fishera.gui.task.TaskMenuBuilder;
import com.sythel.fishera.gui.top.TopListMenu;
import com.sythel.fishera.gui.top.TopListMenuBuilder;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.service.CollectionService;
import com.sythel.fishera.service.EconomyService;
import com.sythel.fishera.service.SellService;
import com.sythel.fishera.service.TaskService;

public class MenuSetup {

    private final ConfigManager configManager;

    private final BaitRegistry baitRegistry;

    private final RodRegistry rodRegistry;

    private final BaitItemBuilder baitItemBuilder;

    private final RodItemBuilder rodItemBuilder;

    private final CollectionService collectionService;

    private final SellService sellService;

    private final EconomyService economyService;

    private final TaskService taskService;

    private MainMenu mainMenu;

    private BaitMenu baitMenu;

    private CollectionMenu collectionMenu;

    private SellMenu sellMenu;

    private RodMenu rodMenu;

    private TopListMenu topListMenu;

    private TaskMenu taskMenu;

    private FishDetailMenu detailMenu;

    public MenuSetup(
            ConfigManager configManager,
            BaitRegistry baitRegistry,
            RodRegistry rodRegistry,
            BaitItemBuilder baitItemBuilder,
            RodItemBuilder rodItemBuilder,
            CollectionService collectionService,
            SellService sellService,
            EconomyService economyService,
            TaskService taskService) {

        this.configManager = configManager;

        this.baitRegistry = baitRegistry;

        this.rodRegistry = rodRegistry;

        this.baitItemBuilder = baitItemBuilder;

        this.rodItemBuilder = rodItemBuilder;

        this.collectionService = collectionService;

        this.sellService = sellService;

        this.economyService = economyService;

        this.taskService = taskService;

    }

    public void initialize() {

        mainMenu =
                new MainMenu(
                        new MainMenuBuilder(
                                configManager
                        ),
                        configManager
                );

        baitMenu =
                new BaitMenu(
                        new BaitMenuBuilder(
                                configManager,
                                baitRegistry,
                                baitItemBuilder
                        ),
                        configManager
                );

        collectionMenu =
                new CollectionMenu(
                        collectionService,
                        new CollectionItemBuilder(
                                configManager
                        ),
                        new CollectionStatisticsBuilder(
                                configManager,
                                collectionService
                        ),
                        configManager
                );

        sellMenu =
                new SellMenu(
                        new SellMenuBuilder(
                                configManager,
                                sellService,
                                economyService
                        ),
                        configManager
                );

        rodMenu =
                new RodMenu(
                        new RodMenuBuilder(
                                configManager,
                                rodRegistry,
                                rodItemBuilder
                        ),
                        configManager
                );

        topListMenu =
                new TopListMenu(
                        new TopListMenuBuilder(
                                configManager
                        ),
                        configManager
                );

        taskMenu =
                new TaskMenu(
                        new TaskMenuBuilder(
                                taskService,
                                configManager
                        ),
                        taskService,
                        configManager
                );

        detailMenu =
                new FishDetailMenu(
                        new FishDetailItemBuilder(
                                configManager
                        ),
                        configManager
                );

    }

    public MainMenu getMainMenu() {

        return mainMenu;

    }

    public BaitMenu getBaitMenu() {

        return baitMenu;

    }

    public CollectionMenu getCollectionMenu() {

        return collectionMenu;

    }

    public SellMenu getSellMenu() {

        return sellMenu;

    }

    public RodMenu getRodMenu() {

        return rodMenu;

    }

    public TopListMenu getTopListMenu() {

        return topListMenu;

    }

    public TaskMenu getTaskMenu() {

        return taskMenu;

    }

    public FishDetailMenu getDetailMenu() {

        return detailMenu;

    }

}