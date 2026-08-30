package com.sythel.fishera.world;

import com.sythel.fishera.config.ConfigManager;
import org.bukkit.World;

public class WorldManager {

    private final ConfigManager configManager;

    public WorldManager(ConfigManager configManager) {

        this.configManager = configManager;

    }

    public boolean isFishingWorld(World world) {

        return configManager
                .getEnabledWorlds()
                .contains(world.getName());

    }

}