package com.sythel.fishera.managers;

import com.sythel.fishera.service.FishingService;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.MinigameService;
import com.sythel.fishera.session.FishingSession;
import com.sythel.fishera.world.WorldManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingManager {

    private final WorldManager worldManager;

    private final FishingSessionManager sessionManager;

    private final FishingService fishingService;

    private final MinigameService minigameService;

    private final MessageService messageService;

    public FishingManager(
            WorldManager worldManager,
            FishingSessionManager sessionManager,
            FishingService fishingService,
            MinigameService minigameService,
            MessageService messageService) {

        this.worldManager = worldManager;

        this.sessionManager = sessionManager;

        this.fishingService = fishingService;

        this.minigameService = minigameService;

        this.messageService = messageService;

    }

    public void startFishing(
            Player player) {

        if (!worldManager.isFishingWorld(
                player.getWorld())) {

            return;

        }

        if (minigameService.isPlaying(
                player)) {

            return;

        }

        if (sessionManager.hasSession(
                player)) {

            return;

        }

        FishingSession session =
                sessionManager.createSession(
                        player
                );

        session.setState(
                FishingSession.State.WAITING
        );

        fishingService.startWaiting(
                session,
                () -> onFishBite(player)
        );

    }

    public void onFishBite(
            Player player) {

        if (!worldManager.isFishingWorld(
                player.getWorld())) {

            return;

        }

        if (minigameService.isPlaying(
                player)) {

            return;

        }

        FishingSession session =
                sessionManager.getSession(
                        player
                );

        if (session == null) {

            return;

        }

        if (session.getState()
                != FishingSession.State.WAITING) {

            return;

        }

        session.setState(
                FishingSession.State.BITING
        );

        messageService.send(
                player,
                "fishing.bite"
        );

    }

    public void catchFish(
            PlayerFishEvent event) {

        Player player =
                event.getPlayer();

        if (!worldManager.isFishingWorld(
                player.getWorld())) {

            return;

        }

        if (minigameService.isPlaying(
                player)) {

            event.setCancelled(true);

            return;

        }

        if (!sessionManager.hasSession(
                player)) {

            return;

        }

        event.setCancelled(true);

    }

    public void endFishing(
            Player player) {

        if (!worldManager.isFishingWorld(
                player.getWorld())) {

            return;

        }

        if (minigameService.isPlaying(
                player)) {

            return;

        }

        FishingSession session =
                sessionManager.getSession(
                        player
                );

        if (session == null) {

            return;

        }

        if (session.getState()
                != FishingSession.State.BITING) {

            fishingService.stop(
                    session
            );

            sessionManager.removeSession(
                    player
            );

            return;

        }

        session.setState(
                FishingSession.State.MINIGAME
        );

        fishingService.stop(
                session
        );

        minigameService.start(
                player
        );

    }

}