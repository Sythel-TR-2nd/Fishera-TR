package com.sythel.fishera.gui.sell;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.EconomyService;
import com.sythel.fishera.service.SellService;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.NumberUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SellMenuBuilder {

    private final ConfigManager configManager;

    private final SellService sellService;

    private final EconomyService economyService;

    public SellMenuBuilder(
            ConfigManager configManager,
            SellService sellService,
            EconomyService economyService) {

        this.configManager = configManager;

        this.sellService = sellService;

        this.economyService = economyService;

    }

    public void build(
            Player player,
            Inventory inventory) {

        fillBackground(inventory);

        inventory.setItem(
                10,
                createButton("sell")
        );

        inventory.setItem(
                12,
                createBalanceItem(player)
        );

        inventory.setItem(
                14,
                createValueItem(player)
        );

        inventory.setItem(
                22,
                createButton("close")
        );

    }

    private ItemStack createBalanceItem(
            Player player) {

        FileConfiguration config =
                configManager.getGuiConfig();

        ItemStack item =
                createButton("balance");

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        String path =
                "gui.sell.buttons.balance";

        List<String> lore =
                config.getStringList(
                        path + ".lore"
                );

        double balance =
                economyService.getBalance(player);

        lore =
                lore.stream()
                        .map(line ->
                                line.replace(
                                        "%balance%",
                                        NumberUtil.formatMoney(balance)
                                )
                        )
                        .toList();

        meta.setLore(
                ColorUtil.color(lore)
        );

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createValueItem(
            Player player) {

        FileConfiguration config =
                configManager.getGuiConfig();

        ItemStack item =
                createButton("value");

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        String path =
                "gui.sell.buttons.value";

        int amount =
                sellService.getFishAmount(player);

        double value =
                sellService.getInventoryValue(player);

        List<String> lore =
                config.getStringList(
                        path + ".lore"
                );

        lore =
                lore.stream()
                        .map(line ->
                                line
                                        .replace(
                                                "%amount%",
                                                String.valueOf(amount)
                                        )
                                        .replace(
                                                "%value%",
                                                NumberUtil.formatMoney(value)
                                        )
                        )
                        .toList();

        meta.setLore(
                ColorUtil.color(lore)
        );

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createButton(
            String id) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.sell.buttons." + id;

        String materialName =
                config.getString(
                        path + ".material",
                        "STONE"
                );

        Material material =
                Material.matchMaterial(
                        materialName
                );

        if (material == null) {

            material = Material.STONE;

        }

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {

            return item;

        }

        String name =
                config.getString(
                        path + ".name",
                        ""
                );

        List<String> lore =
                config.getStringList(
                        path + ".lore"
                );

        meta.setDisplayName(
                ColorUtil.color(name)
        );

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

        FileConfiguration config =
                configManager.getGuiConfig();

        String materialName =
                config.getString(
                        "gui.sell.background.material",
                        "BLACK_STAINED_GLASS_PANE"
                );

        Material material =
                Material.matchMaterial(
                        materialName
                );

        if (material == null) {

            material =
                    Material.BLACK_STAINED_GLASS_PANE;

        }

        ItemStack glass =
                new ItemStack(material);

        ItemMeta meta =
                glass.getItemMeta();

        if (meta != null) {

            String name =
                    config.getString(
                            "gui.sell.background.name",
                            " "
                    );

            meta.setDisplayName(
                    ColorUtil.color(name)
            );

            glass.setItemMeta(meta);

        }

        for (int i = 0;
             i < inventory.getSize();
             i++) {

            inventory.setItem(
                    i,
                    glass
            );

        }

    }

}