package com.sythel.fishera.minigame;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MinigameTask extends BukkitRunnable {

    private final FishingMinigame minigame;

    private final MinigameRenderer renderer = new MinigameRenderer();

    public MinigameTask(JavaPlugin plugin,
                        FishingMinigame minigame) {

        this.minigame = minigame;

        minigame.setTask(
                runTaskTimer(plugin, 0L, 2L)
        );

    }

    @Override
    public void run() {

        if (!minigame.getPlayer().isOnline()) {

            cancel();
            return;

        }

        if (minigame.isMovingRight()) {

            minigame.setCursor(minigame.getCursor() + 1);

            if (minigame.getCursor() >= 16) {

                minigame.setCursor(16);
                minigame.setMovingRight(false);

            }

        } else {

            minigame.setCursor(minigame.getCursor() - 1);

            if (minigame.getCursor() <= 0) {

                minigame.setCursor(0);
                minigame.setMovingRight(true);

            }

        }

        minigame.getPlayer().sendTitle(
                "",
                renderer.render(minigame),
                0,
                5,
                0
        );

    }

}