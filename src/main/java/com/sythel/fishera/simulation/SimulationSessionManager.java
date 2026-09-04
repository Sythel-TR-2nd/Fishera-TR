package com.sythel.fishera.simulation;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationSessionManager {

    private final Map<UUID, SimulationSession> sessions =
            new ConcurrentHashMap<>();

    public SimulationSession get(
            Player player,
            int defaultDuration) {

        return sessions.computeIfAbsent(
                player.getUniqueId(),
                key -> new SimulationSession(
                        defaultDuration
                )
        );
    }

    public void remove(
            Player player) {

        sessions.remove(
                player.getUniqueId()
        );
    }

    public void clear() {

        sessions.clear();
    }
}