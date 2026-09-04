package com.sythel.fishera.simulation;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SimulationSelectionMenu {

    private final EconomySimulationService simulationService;
    private final SimulationConfig simulationConfig;

    public SimulationSelectionMenu(
            EconomySimulationService simulationService,
            SimulationConfig simulationConfig) {

        this.simulationService = simulationService;
        this.simulationConfig = simulationConfig;
    }

    public void openRodSelection(
            Player player,
            String selectedRod) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        54,
                        ColorUtil.color(
                                simulationConfig.getSelectionTitle(
                                        "Olta"
                                )
                        )
                );

        fillBackground(inventory);

        int slot = 0;

        for (RodData rod :
                simulationService
                        .getRodRegistry()
                        .getAll()) {

            if (slot >= 45) {
                break;
            }

            inventory.setItem(
                    slot++,
                    createRodItem(
                            rod,
                            selectedRod
                    )
            );
        }

        inventory.setItem(
                49,
                createBackItem()
        );

        player.openInventory(inventory);
    }

    public void openBaitSelection(
            Player player,
            String selectedBait) {

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        54,
                        ColorUtil.color(
                                simulationConfig.getSelectionTitle(
                                        "Yem"
                                )
                        )
                );

        fillBackground(inventory);

        int slot = 0;

        for (BaitData bait :
                simulationService
                        .getBaitRegistry()
                        .getAll()) {

            if (slot >= 45) {
                break;
            }

            inventory.setItem(
                    slot++,
                    createBaitItem(
                            bait,
                            selectedBait
                    )
            );
        }

        inventory.setItem(
                49,
                createBackItem()
        );

        player.openInventory(inventory);
    }

    private ItemStack createRodItem(
            RodData rod,
            String selectedRod) {

        Material material =
                Material.matchMaterial(
                        rod.getMaterial()
                );

        ItemStack item =
                new ItemStack(
                        material != null
                                ? material
                                : Material.FISHING_ROD
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        rod.getName()
                )
        );

        meta.setLore(
                ColorUtil.color(
                        rod.getLore()
                )
        );

        if (selectedRod != null
                && selectedRod.equalsIgnoreCase(
                        rod.getId()
                )) {

            meta.setDisplayName(
                    ColorUtil.color(
                            rod.getName()
                                    + simulationConfig
                                    .getSelectionSuffix()
                    )
            );
        }

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createBaitItem(
            BaitData bait,
            String selectedBait) {

        Material material =
                Material.matchMaterial(
                        bait.getMaterial()
                );

        ItemStack item =
                new ItemStack(
                        material != null
                                ? material
                                : Material.WHEAT_SEEDS
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        bait.getName()
                )
        );

        meta.setLore(
                ColorUtil.color(
                        List.of(
                                bait.getDescription()
                        )
                )
        );

        if (selectedBait != null
                && selectedBait.equalsIgnoreCase(
                        bait.getId()
                )) {

            meta.setDisplayName(
                    ColorUtil.color(
                            bait.getName()
                                    + simulationConfig
                                    .getSelectionSuffix()
                    )
            );
        }

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
                                .getSelectionBackMaterial()
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(
                        simulationConfig
                                .getSelectionBackName()
                )
        );

        List<String> lore =
                simulationConfig
                        .getSelectionBackLore();

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
                                .getSelectionBackgroundMaterial()
                );

        ItemMeta meta =
                background.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(
                            simulationConfig
                                    .getSelectionBackgroundName()
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

    public String getRodTitle() {

        return ColorUtil.color(
                simulationConfig.getSelectionTitle(
                        "Olta"
                )
        );
    }

    public String getBaitTitle() {

        return ColorUtil.color(
                simulationConfig.getSelectionTitle(
                        "Yem"
                )
        );
    }
}