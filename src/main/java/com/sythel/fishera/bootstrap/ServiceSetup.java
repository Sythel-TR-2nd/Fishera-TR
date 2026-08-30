package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.builder.FishItemBuilder;
import com.sythel.fishera.builder.RodItemBuilder;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.registry.RarityRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.repository.CollectionRepository;
import com.sythel.fishera.repository.FishRepository;
import com.sythel.fishera.repository.TaskRepository;
import com.sythel.fishera.service.CaughtFishFactory;
import com.sythel.fishera.service.CollectionService;
import com.sythel.fishera.service.EconomyService;
import com.sythel.fishera.service.FishSelector;
import com.sythel.fishera.service.FishingRewardService;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.SellService;
import com.sythel.fishera.service.TaskService;
import com.sythel.fishera.task.TaskData;

import java.util.List;

public class ServiceSetup {

    private final Fishera plugin;
    private final FishRegistry fishRegistry;
    private final RarityRegistry rarityRegistry;
    private final RodRegistry rodRegistry;
    private final BaitRegistry baitRegistry;
    private final FishRepository fishRepository;
    private final CollectionRepository collectionRepository;
    private final TaskRepository taskRepository;
    private final List<TaskData> tasks;
    private final MessageService messageService;

    private FishSelector fishSelector;
    private CaughtFishFactory caughtFishFactory;
    private EconomyService economyService;
    private TaskService taskService;
    private FishingRewardService rewardService;
    private CollectionService collectionService;
    private SellService sellService;

    public ServiceSetup(
            Fishera plugin,
            FishRegistry fishRegistry,
            RarityRegistry rarityRegistry,
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry,
            FishRepository fishRepository,
            CollectionRepository collectionRepository,
            TaskRepository taskRepository,
            List<TaskData> tasks,
            MessageService messageService) {

        this.plugin = plugin;
        this.fishRegistry = fishRegistry;
        this.rarityRegistry = rarityRegistry;
        this.rodRegistry = rodRegistry;
        this.baitRegistry = baitRegistry;
        this.fishRepository = fishRepository;
        this.collectionRepository = collectionRepository;
        this.taskRepository = taskRepository;
        this.tasks = tasks;
        this.messageService = messageService;
    }

    public void initialize() {

        FishItemBuilder fishItemBuilder =
                new FishItemBuilder(plugin);

        RodItemBuilder rodItemBuilder =
                new RodItemBuilder(plugin);

        fishSelector =
                new FishSelector(
                        fishRegistry,
                        baitRegistry
                );

        caughtFishFactory =
                new CaughtFishFactory(
                        rarityRegistry,
                        baitRegistry
                );

        economyService =
                new EconomyService(plugin);

        taskService =
                new TaskService(
                        taskRepository,
                        economyService,
                        messageService,
                        tasks
                );

        rewardService =
                new FishingRewardService(
                        fishSelector,
                        caughtFishFactory,
                        fishItemBuilder,
                        fishRepository,
                        rodRegistry,
                        taskService,
                        baitRegistry,
                        messageService
                );

        collectionService =
                new CollectionService(
                        fishRegistry,
                        collectionRepository
                );

        sellService =
                new SellService(
                        economyService,
                        messageService
                );
    }

    public FishSelector getFishSelector() {

        return fishSelector;

    }

    public CaughtFishFactory getCaughtFishFactory() {

        return caughtFishFactory;

    }

    public EconomyService getEconomyService() {

        return economyService;

    }

    public TaskService getTaskService() {

        return taskService;

    }

    public FishingRewardService getRewardService() {

        return rewardService;

    }

    public CollectionService getCollectionService() {

        return collectionService;

    }

    public SellService getSellService() {

        return sellService;

    }

}