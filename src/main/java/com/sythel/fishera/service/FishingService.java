package com.sythel.fishera.service;

import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.scheduler.FishingTask;
import com.sythel.fishera.session.FishingSession;
import org.bukkit.plugin.java.JavaPlugin;

public class FishingService {

    private final JavaPlugin plugin;

    private final RodRegistry rodRegistry;

    private final BaitRegistry baitRegistry;

    public FishingService(
            JavaPlugin plugin,
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry) {

        this.plugin = plugin;

        this.rodRegistry = rodRegistry;

        this.baitRegistry = baitRegistry;

    }

    public void startWaiting(
            FishingSession session,
            Runnable onBite) {

        new FishingTask(
                plugin,
                rodRegistry,
                baitRegistry,
                session,
                onBite
        );

    }

    public void stop(
            FishingSession session) {

        if (session == null) {
            return;
        }

        session.destroy();

    }

}