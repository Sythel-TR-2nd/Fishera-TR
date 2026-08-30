package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.managers.FishingManager;
import com.sythel.fishera.managers.FishingSessionManager;
import com.sythel.fishera.minigame.MinigameManager;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.service.FishingRewardService;
import com.sythel.fishera.service.FishingService;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.MinigameService;
import com.sythel.fishera.world.WorldManager;

public class FishingSetup {

    private final Fishera plugin;

    private final RodRegistry rodRegistry;

    private final BaitRegistry baitRegistry;

    private final FishingRewardService rewardService;

    private final WorldManager worldManager;

    private final MessageService messageService;

    private FishingSessionManager sessionManager;

    private MinigameManager minigameManager;

    private FishingService fishingService;

    private MinigameService minigameService;

    private FishingManager fishingManager;

    public FishingSetup(
            Fishera plugin,
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry,
            FishingRewardService rewardService,
            WorldManager worldManager,
            MessageService messageService) {

        this.plugin = plugin;

        this.rodRegistry = rodRegistry;

        this.baitRegistry = baitRegistry;

        this.rewardService = rewardService;

        this.worldManager = worldManager;

        this.messageService = messageService;
    }

    public void initialize() {

        sessionManager =
                new FishingSessionManager();

        minigameManager =
                new MinigameManager();

        fishingService =
                new FishingService(
                        plugin,
                        rodRegistry,
                        baitRegistry
                );

        minigameService =
                new MinigameService(
                        plugin,
                        minigameManager,
                        sessionManager,
                        rewardService,
                        messageService
                );

        fishingManager =
                new FishingManager(
                        worldManager,
                        sessionManager,
                        fishingService,
                        minigameService,
                        messageService
                );
    }

    public FishingManager getFishingManager() {
        return fishingManager;
    }

    public MinigameService getMinigameService() {
        return minigameService;
    }
}