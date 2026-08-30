package com.sythel.fishera;

import com.sythel.fishera.bootstrap.PluginInitializer;
import com.sythel.fishera.util.ItemUtil;

public final class Fishera extends org.bukkit.plugin.java.JavaPlugin {

    private PluginInitializer initializer;

    @Override
    public void onEnable() {

        getLogger().info("========================================");
        getLogger().info("          FISHERA BASLATILIYOR");
        getLogger().info("========================================");

        ItemUtil.initialize(this);

        initializer =
                new PluginInitializer(this);

        initializer.initialize();

        getLogger().info("----------------------------------------");
        getLogger().info(" Fishera basariyla baslatildi.");
        getLogger().info(" Gelistirici: Sythel");
        getLogger().info("----------------------------------------");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {

        getLogger().info("========================================");
        getLogger().info("          FISHERA KAPATILIYOR");
        getLogger().info("========================================");

        if (initializer != null) {
            initializer.shutdown();
        }

        getLogger().info("----------------------------------------");
        getLogger().info(" Fishera basariyla kapatildi.");
        getLogger().info(" Tum kaynaklar guvenli sekilde kapatildi.");
        getLogger().info("========================================");
    }
}