package com.sythel.fishera.service;

import com.sythel.fishera.managers.FishingSessionManager;
import com.sythel.fishera.minigame.FishingMinigame;
import com.sythel.fishera.minigame.MinigameManager;
import com.sythel.fishera.minigame.MinigameTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinigameService {

    private static final long CLICK_DELAY_TICKS = 2L;

    private final JavaPlugin plugin;

    private final MinigameManager minigameManager;

    private final FishingSessionManager sessionManager;

    private final FishingRewardService rewardService;

    private final MessageService messageService;

    public MinigameService(
            JavaPlugin plugin,
            MinigameManager minigameManager,
            FishingSessionManager sessionManager,
            FishingRewardService rewardService,
            MessageService messageService) {

        this.plugin = plugin;

        this.minigameManager = minigameManager;

        this.sessionManager = sessionManager;

        this.rewardService = rewardService;

        this.messageService = messageService;
    }

    public void start(
            Player player) {

        if (minigameManager.has(player)) {
            return;
        }

        minigameManager.create(player);

        FishingMinigame minigame =
                minigameManager.get(player);

        new MinigameTask(
                plugin,
                minigame
        );

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> {

                    if (minigameManager.has(player)) {

                        minigame.setCanClick(true);

                    }

                },
                CLICK_DELAY_TICKS
        );
    }

    public void stop(
            Player player) {

        FishingMinigame minigame =
                minigameManager.get(player);

        if (minigame == null) {
            return;
        }

        if (minigame.getTask() != null) {

            minigame.getTask().cancel();

        }

        player.resetTitle();

        minigameManager.remove(player);

        sessionManager.removeSession(player);
    }

    public boolean isPlaying(
            Player player) {

        return minigameManager.has(player);
    }

    public void click(
            Player player) {

        FishingMinigame minigame =
                minigameManager.get(player);

        if (minigame == null) {
            return;
        }

        if (!minigame.canClick()) {
            return;
        }

        if (minigame.isSuccess()) {

            rewardService.reward(
                    player
            );

        } else {

            messageService.send(
                    player,
                    "fishing.missed"
            );
        }

        stop(player);
    }
}