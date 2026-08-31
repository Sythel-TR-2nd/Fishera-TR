package com.sythel.fishera.update;

import com.sythel.fishera.Fishera;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

    private final Fishera plugin;

    private final UpdateChecker updateChecker;

    public UpdateListener(
            Fishera plugin,
            UpdateChecker updateChecker) {

        this.plugin = plugin;

        this.updateChecker = updateChecker;
    }

    @EventHandler
    public void onPlayerJoin(
            PlayerJoinEvent event) {

        if (!event.getPlayer().isOp()) {
            return;
        }

        if (!updateChecker.isUpdateAvailable()) {
            return;
        }

        String currentVersion =
                plugin.getDescription()
                        .getVersion();

        String latestVersion =
                updateChecker.getLatestVersion();

        event.getPlayer().sendMessage("");

        event.getPlayer().sendMessage(
                "§6§lFishera §8» §eYeni bir sürüm mevcut!"
        );

        event.getPlayer().sendMessage(
                "§7Mevcut sürüm: §f"
                        + currentVersion
        );

        event.getPlayer().sendMessage(
                "§7Yeni sürüm: §a"
                        + latestVersion
        );

        event.getPlayer().sendMessage(
                "§7İndirmek için: §bhttps://github.com/Sythel-TR-2nd/Fishera-TR/releases/latest"
        );

        event.getPlayer().sendMessage("");
    }
}