package com.sythel.fishera.gui.top;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.top.TopCategory;
import com.sythel.fishera.top.TopEntry;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TopListMenuBuilder {

    private static final int[] TOP_SLOTS = {
            28, 29, 30, 31, 32,
            37, 38, 39, 40, 41
    };

    private final ConfigManager configManager;

    public TopListMenuBuilder(
            ConfigManager configManager) {

        this.configManager = configManager;

    }

    public void build(
            Inventory inventory,
            TopCategory category,
            List<TopEntry> entries) {

        FileConfiguration config =
                configManager.getGuiConfig();

        fillBackground(
                inventory,
                config
        );

        inventory.setItem(
                10,
                createCategoryButton(
                        config,
                        "money",
                        Material.GOLD_INGOT,
                        "&#FFD54F💰 Para"
                )
        );

        inventory.setItem(
                12,
                createCategoryButton(
                        config,
                        "fish",
                        Material.COD,
                        "&#4FC3F7🐟 Balık"
                )
        );

        inventory.setItem(
                14,
                createCategoryButton(
                        config,
                        "weight",
                        Material.NAUTILUS_SHELL,
                        "&#81D4FA⚖ Ağırlık"
                )
        );

        inventory.setItem(
                16,
                createCategoryButton(
                        config,
                        "collection",
                        Material.WRITTEN_BOOK,
                        "&#BA68C8📖 Koleksiyon"
                )
        );

        inventory.setItem(
                49,
                createButton(
                        config,
                        "gui.top.buttons.back",
                        Material.BARRIER,
                        "&#EF5350← Geri"
                )
        );

        if (category == null || entries == null) {
            return;
        }

        for (
                int i = 0;
                i < entries.size() && i < TOP_SLOTS.length;
                i++
        ) {

            inventory.setItem(
                    TOP_SLOTS[i],
                    createEntry(
                            i + 1,
                            entries.get(i),
                            category,
                            config
                    )
            );

        }

    }

    private ItemStack createEntry(
            int place,
            TopEntry entry,
            TopCategory category,
            FileConfiguration config) {

        ItemStack item =
                new ItemStack(
                        Material.PLAYER_HEAD
                );

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        String name =
                config.getString(
                        "gui.top.entry.name",
                        "&6#%place% &f%player%"
                );

        name =
                replace(
                        name,
                        "%place%",
                        String.valueOf(place)
                );

        name =
                replace(
                        name,
                        "%player%",
                        entry.getPlayerName()
                );

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        String value =
                formatValue(
                        entry.getValue(),
                        category
                );

        String valueLine =
                config.getString(
                        "gui.top.entry.value",
                        "&7Değer: &e%value% %extra%"
                );

        valueLine =
                replace(
                        valueLine,
                        "%value%",
                        value
                );

        valueLine =
                replace(
                        valueLine,
                        "%extra%",
                        entry.getExtra()
                );

        List<String> lore =
                new ArrayList<>();

        lore.add("");

        lore.add(
                ColorUtil.color(
                        valueLine
                )
        );

        meta.setLore(lore);

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createCategoryButton(
            FileConfiguration config,
            String key,
            Material defaultMaterial,
            String defaultName) {

        return createButton(
                config,
                "gui.top.buttons." + key,
                defaultMaterial,
                defaultName
        );

    }

    private ItemStack createButton(
            FileConfiguration config,
            String path,
            Material defaultMaterial,
            String defaultName) {

        Material material =
                getMaterial(
                        config.getString(
                                path + ".material",
                                defaultMaterial.name()
                        ),
                        defaultMaterial
                );

        String name =
                config.getString(
                        path + ".name",
                        defaultName
                );

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        List<String> lore =
                config.getStringList(
                        path + ".lore"
                );

        if (!lore.isEmpty()) {

            List<String> coloredLore =
                    new ArrayList<>();

            for (String line : lore) {

                coloredLore.add(
                        ColorUtil.color(line)
                );

            }

            meta.setLore(
                    coloredLore
            );

        }

        meta.addItemFlags(
                ItemFlag.values()
        );

        item.setItemMeta(meta);

        return item;

    }

    private void fillBackground(
            Inventory inventory,
            FileConfiguration config) {

        Material material =
                getMaterial(
                        config.getString(
                                "gui.top.background.material",
                                "BLACK_STAINED_GLASS_PANE"
                        ),
                        Material.BLACK_STAINED_GLASS_PANE
                );

        String name =
                config.getString(
                        "gui.top.background.name",
                        " "
                );

        ItemStack glass =
                new ItemStack(material);

        ItemMeta meta =
                glass.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ColorUtil.color(name)
            );

            glass.setItemMeta(meta);

        }

        for (
                int i = 0;
                i < inventory.getSize();
                i++
        ) {

            inventory.setItem(
                    i,
                    glass
            );

        }

    }

    private Material getMaterial(
            String materialName,
            Material fallback) {

        if (materialName == null) {
            return fallback;
        }

        Material material =
                Material.matchMaterial(
                        materialName
                );

        return material != null
                ? material
                : fallback;

    }

    private String formatValue(
            double value,
            TopCategory category) {

        if (
                category == TopCategory.FISH
                        || category == TopCategory.COLLECTION
        ) {

            return String.format(
                    "%,.0f",
                    value
            );

        }

        return String.format(
                "%,.2f",
                value
        );

    }

    private String replace(
            String text,
            String placeholder,
            String value) {

        if (text == null) {
            return "";
        }

        return text.replace(
                placeholder,
                value == null
                        ? ""
                        : value
        );

    }

}