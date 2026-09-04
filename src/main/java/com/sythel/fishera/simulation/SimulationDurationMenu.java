package com.sythel.fishera.simulation;

import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SimulationDurationMenu {

    private final SimulationConfig simulationConfig;

    public SimulationDurationMenu(
            SimulationConfig simulationConfig) {

        this.simulationConfig = simulationConfig;
    }

    public void open(
            Player player,
            int selectedDuration) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        54,
                        ColorUtil.color(
                                simulationConfig
                                        .getDurationTitle()
                        )
                );

        fillBackground(inventory);

        List<Integer> durations =
                simulationConfig
                        .getDurationOptions();

        int slot = 10;

        for (int duration : durations) {

            if (slot >= 44) {
                break;
            }

            inventory.setItem(
                    slot,
                    createDurationItem(
                            duration,
                            selectedDuration
                    )
            );

            slot++;

            if (slot == 17) {
                slot = 19;
            }

            if (slot == 26) {
                slot = 28;
            }

            if (slot == 35) {
                slot = 37;
            }
        }

        inventory.setItem(
                49,
                createBackItem()
        );

        player.openInventory(inventory);
    }

    private ItemStack createDurationItem(
            int duration,
            int selectedDuration) {

        ItemStack item =
                new ItemStack(
                        simulationConfig
                                .getDurationEntryMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        String durationName =
                simulationConfig
                        .getDurationName(
                                duration
                        );

        String name =
                simulationConfig
                        .getDurationEntryName()
                        .replace(
                                "%duration%",
                                durationName
                        );

        if (duration == selectedDuration) {
            name += simulationConfig
                    .getSelectionSuffix();
        }

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        List<String> lore =
                simulationConfig
                        .getDurationEntryLore();

        meta.setLore(
                ColorUtil.color(lore)
        );

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createBackItem() {

        ItemStack item =
                new ItemStack(
                        simulationConfig
                                .getDurationBackMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        simulationConfig
                                .getDurationBackName()
                )
        );

        meta.setLore(
                ColorUtil.color(
                        simulationConfig
                                .getDurationBackLore()
                )
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
                        simulationConfig
                                .getDurationBackgroundMaterial()
                );

        ItemMeta meta =
                background.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            simulationConfig
                                    .getDurationBackgroundName()
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
                simulationConfig
                        .getDurationTitle()
        );
    }
}