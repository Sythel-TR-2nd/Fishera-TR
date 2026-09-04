package com.sythel.fishera.simulation;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SimulationMenuListener implements Listener {

    private final SimulationMenu simulationMenu;
    private final SimulationSelectionMenu selectionMenu;
    private final SimulationDurationMenu durationMenu;
    private final SimulationResultMenu resultMenu;
    private final SimulationComparisonMenu comparisonMenu;
    private final SimulationSessionManager sessionManager;
    private final SimulationConfig simulationConfig;

    public SimulationMenuListener(
            SimulationMenu simulationMenu,
            SimulationSelectionMenu selectionMenu,
            SimulationDurationMenu durationMenu,
            SimulationResultMenu resultMenu,
            SimulationComparisonMenu comparisonMenu,
            SimulationSessionManager sessionManager,
            SimulationConfig simulationConfig) {

        this.simulationMenu = simulationMenu;
        this.selectionMenu = selectionMenu;
        this.durationMenu = durationMenu;
        this.resultMenu = resultMenu;
        this.comparisonMenu = comparisonMenu;
        this.sessionManager = sessionManager;
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

        if (title.equals(simulationMenu.getTitle())) {
            handleMainMenu(event, player);
            return;
        }

        if (title.equals(resultMenu.getTitle())) {
            handleResultMenu(event, player);
            return;
        }

        if (title.equals(comparisonMenu.getTitle())) {
            handleComparisonMenu(event, player);
        }
    }

    private void handleMainMenu(
            InventoryClickEvent event,
            Player player) {

        event.setCancelled(true);

        if (event.getClickedInventory() == null
                || event.getClickedInventory()
                != event.getView().getTopInventory()) {
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

        switch (event.getSlot()) {

            case 10 -> selectionMenu.openRodSelection(
                    player,
                    session.getRodId()
            );

            case 12 -> selectionMenu.openBaitSelection(
                    player,
                    session.getBaitId()
            );

            case 14 -> durationMenu.open(
                    player,
                    session.getDurationSeconds()
            );

            case 16 -> calculate(
                    player,
                    session
            );

            case 22 -> {
                comparisonMenu.open(
                        player,
                        session.getBaitId()
                );

                sendMessage(
                        player,
                        "comparison-completed"
                );
            }

            case 26 -> close(
                    player
            );

            default -> {
            }
        }
    }

    private void handleResultMenu(
            InventoryClickEvent event,
            Player player) {

        event.setCancelled(true);

        if (event.getClickedInventory() == null
                || event.getClickedInventory()
                != event.getView().getTopInventory()) {
            return;
        }

        close(player);
    }

    private void handleComparisonMenu(
            InventoryClickEvent event,
            Player player) {

        event.setCancelled(true);

        if (event.getClickedInventory() == null
                || event.getClickedInventory()
                != event.getView().getTopInventory()) {
            return;
        }

        close(player);
    }

    private void calculate(
            Player player,
            SimulationSession session) {

        if (session.getRodId() == null) {

            sendMessage(
                    player,
                    "no-rod"
            );

            return;
        }

        if (session.getBaitId() == null) {

            sendMessage(
                    player,
                    "no-bait"
            );
        }

        sendMessage(
                player,
                "calculation-started"
        );

        SimulationResult result =
                simulationMenu
                        .getSimulationService()
                        .calculate(
                                session.getRodId(),
                                session.getBaitId(),
                                session.getDurationSeconds()
                        );

        RodData rod =
                simulationMenu
                        .getSimulationService()
                        .getRod(
                                session.getRodId()
                        );

        BaitData bait =
                session.getBaitId() == null
                        ? null
                        : simulationMenu
                        .getSimulationService()
                        .getBait(
                                session.getBaitId()
                        );

        String rodName =
                rod == null
                        ? session.getRodId()
                        : rod.getName();

        String baitName =
                bait == null
                        ? "Yok"
                        : bait.getName();

        resultMenu.open(
                player,
                rodName,
                baitName,
                session.getDurationSeconds(),
                result
        );

        sendMessage(
                player,
                "calculation-completed"
        );
    }

    private void close(
            Player player) {

        player.closeInventory();
        sessionManager.remove(player);
    }

    private void sendMessage(
            Player player,
            String key) {

        List<String> messages =
                simulationConfig
                        .getMessage(key);

        for (String message : messages) {

            player.sendMessage(
                    ColorUtil.color(message)
            );
        }
    }
}