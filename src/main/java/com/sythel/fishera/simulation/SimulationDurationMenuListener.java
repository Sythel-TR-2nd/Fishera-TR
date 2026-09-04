package com.sythel.fishera.simulation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class SimulationDurationMenuListener implements Listener {

    private final SimulationDurationMenu durationMenu;
    private final SimulationSessionManager sessionManager;
    private final SimulationMenu simulationMenu;
    private final SimulationConfig simulationConfig;

    public SimulationDurationMenuListener(
            SimulationDurationMenu durationMenu,
            SimulationSessionManager sessionManager,
            SimulationMenu simulationMenu,
            SimulationConfig simulationConfig) {

        this.durationMenu = durationMenu;
        this.sessionManager = sessionManager;
        this.simulationMenu = simulationMenu;
        this.simulationConfig = simulationConfig;
    }

    @EventHandler
    public void onInventoryClick(
            InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!event.getView()
                .getTitle()
                .equals(durationMenu.getTitle())) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() == null
                || event.getClickedInventory()
                != event.getView().getTopInventory()) {
            return;
        }

        int slot = event.getSlot();

        if (slot == 49) {
            simulationMenu.open(player);
            return;
        }

        List<Integer> durations =
                simulationConfig.getDurationOptions();

        int durationIndex =
                getDurationIndex(slot);

        if (durationIndex < 0
                || durationIndex >= durations.size()) {
            return;
        }

        int duration =
                durations.get(durationIndex);

        SimulationSession session =
                sessionManager.get(
                        player,
                        simulationConfig.getDefaultDuration()
                );

        session.setDurationSeconds(duration);

        simulationMenu.open(player);
    }

    private int getDurationIndex(
            int slot) {

        if (slot >= 10 && slot <= 16) {
            return slot - 10;
        }

        if (slot >= 19 && slot <= 25) {
            return slot - 19 + 7;
        }

        if (slot >= 28 && slot <= 34) {
            return slot - 28 + 14;
        }

        if (slot >= 37 && slot <= 43) {
            return slot - 37 + 21;
        }

        return -1;
    }
}