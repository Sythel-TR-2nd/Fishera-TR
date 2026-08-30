package com.sythel.fishera.minigame;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinigameManager {

    private final Map<UUID, FishingMinigame> minigames = new HashMap<>();

    public void create(Player player) {

        minigames.put(
                player.getUniqueId(),
                new FishingMinigame(player)
        );

    }

    public FishingMinigame get(Player player) {

        return minigames.get(player.getUniqueId());

    }

    public boolean has(Player player) {

        return minigames.containsKey(player.getUniqueId());

    }

    public void remove(Player player) {

        minigames.remove(player.getUniqueId());

    }

}