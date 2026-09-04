package com.sythel.fishera.simulation;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.rod.RodData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SimulationSelectionMenuListener implements Listener {

    private final SimulationSelectionMenu selectionMenu;
    private final SimulationSessionManager sessionManager;
    private final SimulationMenu simulationMenu;
    private final SimulationConfig simulationConfig;

    public SimulationSelectionMenuListener(
            SimulationSelectionMenu selectionMenu,
            SimulationSessionManager sessionManager,
            SimulationMenu simulationMenu,
            SimulationConfig simulationConfig) {

        this.selectionMenu = selectionMenu;
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

        String title =
                event.getView().getTitle();

        boolean rodMenu =
                title.equals(
                        selectionMenu.getRodTitle()
                );

        boolean baitMenu =
                title.equals(
                        selectionMenu.getBaitTitle()
                );

        if (!rodMenu && !baitMenu) {
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

        if (slot < 0 || slot >= 45) {
            return;
        }

        ItemStack item =
                event.getCurrentItem();

        if (item == null
                || item.getType().isAir()) {
            return;
        }

        SimulationSession session =
                sessionManager.get(
                        player,
                        simulationConfig.getDefaultDuration()
                );

        if (rodMenu) {

            List<RodData> rods =
                    new ArrayList<>(
                            simulationMenu
                                    .getSimulationService()
                                    .getRodRegistry()
                                    .getAll()
                    );

            if (slot >= rods.size()) {
                return;
            }

            RodData rod =
                    rods.get(slot);

            session.setRodId(
                    rod.getId()
            );

            simulationMenu.open(player);
            return;
        }

        List<BaitData> baits =
                new ArrayList<>(
                        simulationMenu
                                .getSimulationService()
                                .getBaitRegistry()
                                .getAll()
                );

        if (slot >= baits.size()) {
            return;
        }

        BaitData bait =
                baits.get(slot);

        session.setBaitId(
                bait.getId()
        );

        simulationMenu.open(player);
    }
}