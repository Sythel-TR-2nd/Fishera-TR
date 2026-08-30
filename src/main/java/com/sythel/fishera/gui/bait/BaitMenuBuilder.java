package com.sythel.fishera.gui.bait;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.builder.BaitItemBuilder;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BaitMenuBuilder {

    private static final int[] SLOTS = {
            9, 11, 13, 15, 17,
            18, 20, 22, 24, 26
    };

    private final ConfigManager configManager;

    private final BaitRegistry baitRegistry;

    private final BaitItemBuilder baitItemBuilder;

    public BaitMenuBuilder(
            ConfigManager configManager,
            BaitRegistry baitRegistry,
            BaitItemBuilder baitItemBuilder) {

        this.configManager = configManager;

        this.baitRegistry = baitRegistry;

        this.baitItemBuilder = baitItemBuilder;

    }

    public void build(
            Player player,
            Inventory inventory,
            int page) {

        fillBackground(inventory);

        List<BaitData> baits =
                new ArrayList<>(
                        baitRegistry.getAll()
                );

        int start =
                page * SLOTS.length;

        int end =
                Math.min(
                        start + SLOTS.length,
                        baits.size()
                );

        int slotIndex = 0;

        for (int i = start; i < end; i++) {

            BaitData bait =
                    baits.get(i);

            inventory.setItem(
                    SLOTS[slotIndex++],
                    baitItemBuilder.buildGuiItem(
                            bait
                    )
            );

        }

        int maxPage =
                Math.max(
                        0,
                        (baits.size() - 1)
                                / SLOTS.length
                );

        if (page > 0) {

            inventory.setItem(
                    27,
                    createButton(
                            "previous-page"
                    )
            );

        }

        inventory.setItem(
                31,
                createButton(
                        "main-menu"
                )
        );

        if (page < maxPage) {

            inventory.setItem(
                    35,
                    createButton(
                            "next-page"
                    )
            );

        }

    }

    private ItemStack createButton(
            String id) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.bait.buttons." + id;

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

        item.setItemMeta(meta);

        return item;

    }

    private void fillBackground(
            Inventory inventory) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String materialName =
                config.getString(
                        "gui.bait.background.material",
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
                            "gui.bait.background.name",
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