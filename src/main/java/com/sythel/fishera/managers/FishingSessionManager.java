package com.sythel.fishera.managers;

import com.sythel.fishera.session.FishingSession;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingSessionManager {

    private final Map<UUID, FishingSession> sessions = new HashMap<>();

    public FishingSession createSession(Player player) {

        FishingSession session = new FishingSession(player);

        sessions.put(player.getUniqueId(), session);

        return session;

    }

    public FishingSession getSession(Player player) {

        return sessions.get(player.getUniqueId());

    }

    public boolean hasSession(Player player) {

        return sessions.containsKey(player.getUniqueId());

    }

    public void removeSession(Player player) {

        FishingSession session = sessions.remove(player.getUniqueId());

        if (session != null) {
            session.destroy();
        }

    }

    public void clear() {

        sessions.values().forEach(FishingSession::destroy);

        sessions.clear();

    }

}