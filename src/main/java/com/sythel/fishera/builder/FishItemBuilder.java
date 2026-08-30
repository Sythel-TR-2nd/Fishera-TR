package com.sythel.fishera.builder;

import com.sythel.fishera.fish.CaughtFish;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.PlaceholderUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class FishItemBuilder {

    private final NamespacedKey fishIdKey;
    private final NamespacedKey fishPriceKey;

    public FishItemBuilder(JavaPlugin plugin) {

        this.fishIdKey =
                new NamespacedKey(plugin, "fish_id");

        this.fishPriceKey =
                new NamespacedKey(plugin, "fish_price");

    }

    public ItemStack build(CaughtFish fish) {

        Material material = Material.matchMaterial(
                fish.getFishData().getMaterial()
        );

        if (material == null) {
            material = Material.COD;
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
                        fish.getFishData().getName()
                )
        );

        if (fish.getFishData().getCustomModelData() > 0) {

            meta.setCustomModelData(
                    fish.getFishData().getCustomModelData()
            );

        }

        meta.setLore(
                ColorUtil.color(
                        PlaceholderUtil.apply(
                                fish.getFishData().getLore(),
                                fish
                        )
                )
        );

        meta.getPersistentDataContainer().set(
                fishIdKey,
                PersistentDataType.STRING,
                fish.getFishData().getId()
        );

        meta.getPersistentDataContainer().set(
                fishPriceKey,
                PersistentDataType.DOUBLE,
                fish.getPrice()
        );

        item.setItemMeta(meta);

        return item;

    }

}