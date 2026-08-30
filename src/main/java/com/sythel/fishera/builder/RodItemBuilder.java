package com.sythel.fishera.builder;

import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.RarityUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RodItemBuilder {

    private final NamespacedKey rodIdKey;

    public RodItemBuilder(JavaPlugin plugin) {

        this.rodIdKey =
                new NamespacedKey(
                        plugin,
                        "rod_id"
                );

    }

    public ItemStack build(RodData rod) {

        return createItem(
                rod,
                false
        );

    }

    public ItemStack buildGuiItem(RodData rod) {

        return createItem(
                rod,
                true
        );

    }

    private ItemStack createItem(
            RodData rod,
            boolean gui) {

        Material material =
                Material.matchMaterial(
                        rod.getMaterial()
                );

        if (material == null) {
            material = Material.FISHING_ROD;
        }

        ItemStack item =
                new ItemStack(material);

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

        if (rod.getCustomModelData() > 0) {

            meta.setCustomModelData(
                    rod.getCustomModelData()
            );

        }

        List<String> lore =
                new ArrayList<>(
                        ColorUtil.color(
                                rod.getLore()
                        )
                );

        if (gui) {

            lore.add("");

            lore.add(
                    ColorUtil.color(
                            "&7Maksimum Ağırlık: &f"
                                    + rod.getMaxWeight()
                                    + " KG"
                    )
            );

            lore.add(
                    ColorUtil.color(
                            "&7Maksimum Nadirlik: "
                    )
                            + RarityUtil.getDisplayName(
                            rod.getMaxRarity()
                    )
            );

            lore.add("");

            lore.add(
                    ColorUtil.color(
                            "&6Fiyat: &e"
                                    + String.format(
                                    "%,.2f",
                                    rod.getPrice()
                            )
                                    + " ⛃"
                    )
            );

            lore.add("");

            lore.add(
                    ColorUtil.color(
                            "&a▶ Satın almak için tıkla."
                    )
            );

        }

        meta.setLore(lore);

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES
        );

        meta.getPersistentDataContainer().set(
                rodIdKey,
                PersistentDataType.STRING,
                rod.getId()
        );

        item.setItemMeta(meta);

        return item;

    }

}