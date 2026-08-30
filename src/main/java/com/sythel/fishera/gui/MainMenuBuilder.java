package com.sythel.fishera.gui;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MainMenuBuilder {

    private final ConfigManager configManager;

    public MainMenuBuilder(
            ConfigManager configManager) {

        this.configManager = configManager;

    }

    public void build(
            Inventory inventory) {

        fillBackground(inventory);

        inventory.setItem(
                10,
                createMenuItem("rods")
        );

        inventory.setItem(
                12,
                createMenuItem("bait")
        );

        inventory.setItem(
                14,
                createMenuItem("sell")
        );

        inventory.setItem(
                16,
                createMenuItem("collection")
        );

        inventory.setItem(
                21,
                createMenuItem("top")
        );

        inventory.setItem(
                23,
                createMenuItem("tasks")
        );

    }

    private void fillBackground(
            Inventory inventory) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String materialName =
                config.getString(
                        "gui.main.background.material",
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
                            "gui.main.background.name",
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

    private ItemStack createMenuItem(
            String id) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.main.items." + id;

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

}