package com.sythel.fishera.builder;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BaitItemBuilder {

    private final NamespacedKey baitIdKey;

    public BaitItemBuilder(JavaPlugin plugin) {

        this.baitIdKey =
                new NamespacedKey(
                        plugin,
                        "bait_id"
                );

    }

    public ItemStack build(BaitData bait,
                           int amount) {

        Material material =
                Material.matchMaterial(
                        bait.getMaterial()
                );

        if (material == null) {
            material = Material.WHEAT_SEEDS;
        }

        int safeAmount =
                Math.max(
                        1,
                        Math.min(
                                amount,
                                64
                        )
                );

        ItemStack item =
                new ItemStack(
                        material,
                        safeAmount
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

        if (bait.getCustomModelData() > 0) {

            meta.setCustomModelData(
                    bait.getCustomModelData()
            );

        }

        List<String> lore =
                new ArrayList<>();

        lore.add(
                ColorUtil.color(
                        bait.getDescription()
                )
        );

        lore.add("");

        if (bait.getWeight() != 0) {

            lore.add(
                    ColorUtil.color(
                            "&7⚖ Ağırlık: &f+"
                                    + bait.getWeight()
                    )
            );

        }

        if (bait.getSpeed() != 0) {

            lore.add(
                    ColorUtil.color(
                            "&7⚡ Hız: &f+"
                                    + bait.getSpeed()
                    )
            );

        }

        if (bait.getLuck() != 0) {

            lore.add(
                    ColorUtil.color(
                            "&7☘ Şans: &f+"
                                    + bait.getLuck()
                    )
            );

        }

        lore.add("");

        lore.add(
                ColorUtil.color(
                        "&6Fiyat: &e"
                                + String.format(
                                "%,.2f",
                                bait.getPrice()
                        )
                                + " ⛃"
                )
        );

        meta.setLore(lore);

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES
        );

        meta.getPersistentDataContainer().set(
                baitIdKey,
                PersistentDataType.STRING,
                bait.getId()
        );

        item.setItemMeta(meta);

        return item;

    }

    public ItemStack buildGuiItem(BaitData bait) {

        return build(
                bait,
                1
        );

    }

}