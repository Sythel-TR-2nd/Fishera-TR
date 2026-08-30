package com.sythel.fishera.listeners;

import com.sythel.fishera.managers.FishingManager;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.MinigameService;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerFishListener implements Listener {

    private final JavaPlugin plugin;

    private final FishingManager fishingManager;

    private final MinigameService minigameService;

    private final MessageService messageService;

    private final Map<UUID, BukkitTask> waterChecks =
            new HashMap<>();

    public PlayerFishListener(
            JavaPlugin plugin,
            FishingManager fishingManager,
            MinigameService minigameService,
            MessageService messageService) {

        this.plugin = plugin;

        this.fishingManager = fishingManager;

        this.minigameService = minigameService;

        this.messageService = messageService;

    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {

        Player player =
                event.getPlayer();

        if (event.getState()
                == PlayerFishEvent.State.REEL_IN
                && minigameService.isPlaying(player)) {

            event.setCancelled(true);

            stopWaterCheck(player);

            return;

        }

        if (event.getState()
                == PlayerFishEvent.State.FISHING) {

            ItemStack item =
                    player.getInventory()
                            .getItemInMainHand();

            if (!ItemUtil.isRod(item)) {

                event.setCancelled(true);

                messageService.send(
                        player,
                        "general.invalid-rod"
                );

                return;

            }

            startWaterCheck(
                    player,
                    event.getHook()
            );

            return;

        }

        switch (event.getState()) {

            case BITE:

                fishingManager.onFishBite(player);

                break;

            case CAUGHT_FISH:

                fishingManager.catchFish(event);

                break;

            case REEL_IN:

                stopWaterCheck(player);

                fishingManager.endFishing(player);

                break;

            default:

                break;

        }

    }

    private void startWaterCheck(
            Player player,
            FishHook hook) {

        stopWaterCheck(player);

        UUID uuid =
                player.getUniqueId();

        BukkitTask task =
                Bukkit.getScheduler()
                        .runTaskTimer(
                                plugin,
                                () -> {

                                    if (!player.isOnline()) {

                                        stopWaterCheck(player);

                                        return;

                                    }

                                    if (hook.isDead()
                                            || !hook.isValid()) {

                                        stopWaterCheck(player);

                                        return;

                                    }

                                    if (isInWater(hook)) {

                                        stopWaterCheck(player);

                                        fishingManager.startFishing(player);

                                    }

                                },
                                1L,
                                1L
                        );

        waterChecks.put(
                uuid,
                task
        );

    }

    private boolean isInWater(
            FishHook hook) {

        Material material =
                hook.getLocation()
                        .getBlock()
                        .getType();

        return material == Material.WATER
                || material == Material.BUBBLE_COLUMN;

    }

    private void stopWaterCheck(
            Player player) {

        UUID uuid =
                player.getUniqueId();

        BukkitTask task =
                waterChecks.remove(uuid);

        if (task != null) {

            task.cancel();

        }

    }

}