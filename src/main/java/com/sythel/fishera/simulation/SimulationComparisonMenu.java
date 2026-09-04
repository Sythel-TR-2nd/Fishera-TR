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
import java.util.Locale;

public class SimulationComparisonMenu {

    private final EconomySimulationService simulationService;
    private final SimulationConfig simulationConfig;

    public SimulationComparisonMenu(
            EconomySimulationService simulationService,
            SimulationConfig simulationConfig) {

        this.simulationService = simulationService;
        this.simulationConfig = simulationConfig;
    }

    public void open(
            Player player,
            String baitId) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        54,
                        ColorUtil.color(
                                simulationConfig
                                        .getComparisonTitle()
                        )
                );

        fillBackground(inventory);

        List<RodSimulationEntry> results =
                simulationService.compareRods(
                        baitId == null
                                ? null
                                : simulationService.getBait(baitId)
                );

        int slot = 0;
        int place = 1;

        for (RodSimulationEntry entry : results) {

            if (slot >= 45) {
                break;
            }

            inventory.setItem(
                    slot,
                    createEntryItem(
                            entry,
                            place
                    )
            );

            slot++;
            place++;
        }

        if (results.isEmpty()) {

            inventory.setItem(
                    22,
                    createEmptyItem()
            );
        }

        player.openInventory(inventory);
    }

    private ItemStack createEntryItem(
            RodSimulationEntry entry,
            int place) {

        ItemStack item =
                new ItemStack(
                        simulationConfig
                                .getComparisonEntryMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        SimulationResult result =
                entry.getResult();

        String name =
                simulationConfig
                        .getComparisonEntryName()
                        .replace(
                                "%place%",
                                String.valueOf(place)
                        )
                        .replace(
                                "%rod%",
                                entry.getRod().getName()
                        );

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        List<String> lore =
                new ArrayList<>();

        for (String line :
                simulationConfig
                        .getComparisonEntryLore()) {

            lore.add(
                    line
                            .replace(
                                    "%place%",
                                    String.valueOf(place)
                            )
                            .replace(
                                    "%rod%",
                                    entry.getRod().getName()
                            )
                            .replace(
                                    "%fish-per-hour%",
                                    formatFish(
                                            result.getCatchesPerHour()
                                    )
                            )
                            .replace(
                                    "%average-value%",
                                    formatMoney(
                                            result.getAverageFishValue()
                                    )
                            )
                            .replace(
                                    "%gross%",
                                    formatMoney(
                                            result.getGrossIncome()
                                    )
                            )
                            .replace(
                                    "%bait-cost%",
                                    formatMoney(
                                            result.getBaitCost()
                                    )
                            )
                            .replace(
                                    "%net%",
                                    formatMoney(
                                            result.getNetIncome()
                                    )
                            )
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

    private ItemStack createEmptyItem() {

        ItemStack item =
                new ItemStack(
                        simulationConfig
                                .getComparisonEmptyMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        simulationConfig
                                .getComparisonEmptyName()
                )
        );

        meta.setLore(
                ColorUtil.color(
                        simulationConfig
                                .getComparisonEmptyLore()
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
                                .getBackgroundMaterial()
                );

        ItemMeta meta =
                background.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            simulationConfig
                                    .getBackgroundName()
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

    private String formatFish(
            double value) {

        return String.format(
                Locale.US,
                "%.0f",
                value
        );
    }

    private String formatMoney(
            double value) {

        return String.format(
                Locale.US,
                "%,.2f",
                value
        );
    }

    public String getTitle() {

        return ColorUtil.color(
                simulationConfig
                        .getComparisonTitle()
        );
    }
}