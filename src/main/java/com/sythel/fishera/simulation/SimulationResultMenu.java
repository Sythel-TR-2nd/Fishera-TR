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

public class SimulationResultMenu {

    private final SimulationConfig simulationConfig;

    public SimulationResultMenu(
            SimulationConfig simulationConfig) {

        this.simulationConfig = simulationConfig;
    }

    public void open(
            Player player,
            String rodName,
            String baitName,
            int duration,
            SimulationResult result) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        27,
                        ColorUtil.color(
                                simulationConfig
                                        .getResultName()
                        )
                );

        fillBackground(inventory);

        inventory.setItem(
                13,
                createResultItem(
                        rodName,
                        baitName,
                        duration,
                        result
                )
        );

        player.openInventory(inventory);
    }

    private ItemStack createResultItem(
            String rodName,
            String baitName,
            int duration,
            SimulationResult result) {

        ItemStack item =
                new ItemStack(
                        simulationConfig
                                .getResultMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        simulationConfig
                                .getResultName()
                )
        );

        List<String> lore =
                new ArrayList<>();

        for (String line :
                simulationConfig.getResultLore()) {

            lore.add(
                    line
                            .replace(
                                    "%duration%",
                                    formatDuration(duration)
                            )
                            .replace(
                                    "%rod%",
                                    rodName
                            )
                            .replace(
                                    "%bait%",
                                    baitName
                            )
                            .replace(
                                    "%fish%",
                                    formatFish(
                                            result.getCatchesPerHour()
                                    )
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
                            .replace(
                                    "%success-assumption%",
                                    simulationConfig
                                            .getSuccessAssumption()
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

    private String formatDuration(
            int seconds) {

        if (seconds < 60) {
            return seconds + " Saniye";
        }

        if (seconds % 3600 == 0) {
            return (seconds / 3600) + " Saat";
        }

        return (seconds / 60) + " Dakika";
    }

    public String getTitle() {

        return ColorUtil.color(
                simulationConfig
                        .getResultName()
        );
    }
}