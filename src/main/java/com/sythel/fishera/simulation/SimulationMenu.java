package com.sythel.fishera.simulation;

import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SimulationMenu {

    private static final int SIZE = 27;

    private final EconomySimulationService simulationService;
    private final SimulationConfig simulationConfig;
    private final SimulationSessionManager sessionManager;

    public SimulationMenu(
            EconomySimulationService simulationService,
            SimulationConfig simulationConfig,
            SimulationSessionManager sessionManager) {

        this.simulationService = simulationService;
        this.simulationConfig = simulationConfig;
        this.sessionManager = sessionManager;
    }

    public void open(Player player) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        SIZE,
                        ColorUtil.color(
                                simulationConfig.getTitle()
                        )
                );

        SimulationSession session =
                sessionManager.get(
                        player,
                        simulationConfig.getDefaultDuration()
                );

        build(
                inventory,
                session
        );

        player.openInventory(inventory);
    }

    private void build(
            Inventory inventory,
            SimulationSession session) {

        fillBackground(inventory);

        inventory.setItem(
                10,
                createButton(
                        "rod",
                        session
                )
        );

        inventory.setItem(
                12,
                createButton(
                        "bait",
                        session
                )
        );

        inventory.setItem(
                14,
                createButton(
                        "duration",
                        session
                )
        );

        inventory.setItem(
                16,
                createButton(
                        "simulate",
                        session
                )
        );

        inventory.setItem(
                22,
                createButton(
                        "compare",
                        session
                )
        );

        inventory.setItem(
                26,
                createButton(
                        "back",
                        session
                )
        );
    }

    private ItemStack createButton(
            String id,
            SimulationSession session) {

        ItemStack item =
                new ItemStack(
                        simulationConfig.getButtonMaterial(id)
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        simulationConfig.getButtonName(id)
                )
        );

        List<String> lore =
                new ArrayList<>(
                        simulationConfig.getButtonLore(id)
                );

        String rodName = "Yok";
        if (session.getRodId() != null) {
            var rod = simulationService.getRodRegistry().get(session.getRodId());
            if (rod != null) {
                rodName = rod.getName();
            }
        }

        String baitName = "Yok";
        if (session.getBaitId() != null) {
            var bait = simulationService.getBaitRegistry().get(session.getBaitId());
            if (bait != null) {
                baitName = bait.getName();
            }
        }

        String duration =
                simulationConfig.getDurationName(
                        session.getDurationSeconds()
                );

        for (int i = 0; i < lore.size(); i++) {
            lore.set(
                    i,
                    lore.get(i)
                            .replace("%rod%", rodName)
                            .replace("%bait%", baitName)
                            .replace("%duration%", duration)
            );
        }

        meta.setLore(
                ColorUtil.color(lore)
        );

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;
    }

    private void fillBackground(
            Inventory inventory) {

        ItemStack background =
                new ItemStack(
                        simulationConfig.getBackgroundMaterial()
                );

        ItemMeta meta =
                background.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            simulationConfig.getBackgroundName()
                    )
            );

            meta.addItemFlags(
                    ItemFlag.values()
            );

            background.setItemMeta(meta);
        }

        for (int slot = 0;
             slot < inventory.getSize();
             slot++) {

            inventory.setItem(
                    slot,
                    background
            );
        }
    }

    public String getTitle() {

        return ColorUtil.color(
                simulationConfig.getTitle()
        );
    }

    public EconomySimulationService getSimulationService() {

        return simulationService;
    }

    public SimulationConfig getSimulationConfig() {

        return simulationConfig;
    }
}