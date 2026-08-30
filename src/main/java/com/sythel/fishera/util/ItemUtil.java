package com.sythel.fishera.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemUtil {

    private static NamespacedKey fishIdKey;

    private static NamespacedKey fishPriceKey;

    private static NamespacedKey rodIdKey;

    private static NamespacedKey baitIdKey;

    private static NamespacedKey baitAmountKey;

    private ItemUtil() {
    }

    public static void initialize(
            JavaPlugin plugin) {

        fishIdKey =
                new NamespacedKey(
                        plugin,
                        "fish_id"
                );

        fishPriceKey =
                new NamespacedKey(
                        plugin,
                        "fish_price"
                );

        rodIdKey =
                new NamespacedKey(
                        plugin,
                        "rod_id"
                );

        baitIdKey =
                new NamespacedKey(
                        plugin,
                        "bait_id"
                );

        baitAmountKey =
                new NamespacedKey(
                        plugin,
                        "bait_amount"
                );

    }

    public static boolean isFish(
            ItemStack item) {

        if (item == null) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container =
                meta.getPersistentDataContainer();

        return container.has(
                fishIdKey,
                PersistentDataType.STRING
        );

    }

    public static String getFishId(
            ItemStack item) {

        if (!isFish(item)) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer()
                .get(
                        fishIdKey,
                        PersistentDataType.STRING
                );

    }

    public static double getFishPrice(
            ItemStack item) {

        if (!isFish(item)) {
            return 0D;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return 0D;
        }

        Double value =
                meta.getPersistentDataContainer()
                        .get(
                                fishPriceKey,
                                PersistentDataType.DOUBLE
                        );

        return value == null
                ? 0D
                : value;

    }

    public static boolean isRod(
            ItemStack item) {

        if (item == null) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container =
                meta.getPersistentDataContainer();

        return container.has(
                rodIdKey,
                PersistentDataType.STRING
        );

    }

    public static String getRodId(
            ItemStack item) {

        if (!isRod(item)) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer()
                .get(
                        rodIdKey,
                        PersistentDataType.STRING
                );

    }

    public static boolean isBait(
            ItemStack item) {

        if (item == null) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container =
                meta.getPersistentDataContainer();

        return container.has(
                baitIdKey,
                PersistentDataType.STRING
        );

    }

    public static String getBaitId(
            ItemStack item) {

        if (!isBait(item)) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer()
                .get(
                        baitIdKey,
                        PersistentDataType.STRING
                );

    }

    public static int getBaitAmount(
            ItemStack item) {

        if (!isRod(item)) {
            return 0;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return 0;
        }

        Integer value =
                meta.getPersistentDataContainer()
                        .get(
                                baitAmountKey,
                                PersistentDataType.INTEGER
                        );

        return value == null
                ? 0
                : value;

    }

    public static String getBaitIdFromRod(
            ItemStack item) {

        if (!isRod(item)) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer()
                .get(
                        baitIdKey,
                        PersistentDataType.STRING
                );

    }

    public static void setBait(
            ItemStack item,
            String baitId,
            int amount) {

        if (!isRod(item)) {
            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer container =
                meta.getPersistentDataContainer();

        if (baitId == null || amount <= 0) {

            container.remove(
                    baitIdKey
            );

            container.remove(
                    baitAmountKey
            );

        } else {

            container.set(
                    baitIdKey,
                    PersistentDataType.STRING,
                    baitId
            );

            container.set(
                    baitAmountKey,
                    PersistentDataType.INTEGER,
                    amount
            );

        }

        item.setItemMeta(
                meta
        );

    }

    public static void removeBait(
            ItemStack item) {

        if (!isRod(item)) {
            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer container =
                meta.getPersistentDataContainer();

        container.remove(
                baitIdKey
        );

        container.remove(
                baitAmountKey
        );

        item.setItemMeta(
                meta
        );

    }

    public static int consumeBait(
            ItemStack item) {

        if (!isRod(item)) {
            return 0;
        }

        int amount =
                getBaitAmount(item);

        if (amount <= 0) {
            return 0;
        }

        int newAmount =
                amount - 1;

        if (newAmount <= 0) {

            removeBait(
                    item
            );

            return 0;

        }

        String baitId =
                getBaitIdFromRod(
                        item
                );

        if (baitId == null) {
            return 0;
        }

        setBait(
                item,
                baitId,
                newAmount
        );

        return newAmount;

    }

}