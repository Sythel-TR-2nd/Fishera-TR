package com.sythel.fishera.listeners;

import com.sythel.fishera.service.MinigameService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickListener implements Listener {

    private final MinigameService minigameService;

    public PlayerClickListener(
            MinigameService minigameService) {

        this.minigameService = minigameService;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent event) {

        Player player =
                event.getPlayer();

        if (!minigameService.isPlaying(player)) {
            return;
        }

        Action action =
                event.getAction();

        if (action == Action.LEFT_CLICK_AIR
                || action == Action.LEFT_CLICK_BLOCK
                || action == Action.RIGHT_CLICK_AIR
                || action == Action.RIGHT_CLICK_BLOCK) {

            event.setCancelled(true);

            minigameService.click(player);

        }

    }

}