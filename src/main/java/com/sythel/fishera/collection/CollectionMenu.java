package com.sythel.fishera.collection;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.service.CollectionService;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CollectionMenu {

    private static final int INVENTORY_SIZE = 54;

    private static final int FISH_SLOTS = 45;

    private final CollectionService collectionService;

    private final CollectionItemBuilder itemBuilder;

    private final CollectionStatisticsBuilder statisticsBuilder;

    private final ConfigManager configManager;

    private final Map<UUID, List<CollectionEntry>> openedCollections =
            new HashMap<>();

    public CollectionMenu(
            CollectionService collectionService,
            CollectionItemBuilder itemBuilder,
            CollectionStatisticsBuilder statisticsBuilder,
            ConfigManager configManager) {

        this.collectionService = collectionService;

        this.itemBuilder = itemBuilder;

        this.statisticsBuilder = statisticsBuilder;

        this.configManager = configManager;

    }

    public void open(Player player) {

        List<CollectionEntry> entries =
                collectionService.getCollection(player);

        openedCollections.put(
                player.getUniqueId(),
                entries
        );

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.collection.title",
                        "Balık Koleksiyonu"
                );

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        INVENTORY_SIZE,
                        ColorUtil.color(title)
                );

        ItemStack background =
                createBackgroundItem();

        for (int slot = 0; slot < INVENTORY_SIZE; slot++) {

            inventory.setItem(
                    slot,
                    background
            );

        }

        int slot = 0;

        for (CollectionEntry entry : entries) {

            if (slot >= FISH_SLOTS) {

                break;

            }

            inventory.setItem(
                    slot++,
                    itemBuilder.build(entry)
            );

        }

        buildNavigation(
                inventory,
                player
        );

        player.openInventory(
                inventory
        );

    }

    public CollectionEntry getEntry(
            Player player,
            int slot) {

        if (slot < 0 || slot >= FISH_SLOTS) {

            return null;

        }

        List<CollectionEntry> entries =
                openedCollections.get(
                        player.getUniqueId()
                );

        if (entries == null) {

            return null;

        }

        if (slot >= entries.size()) {

            return null;

        }

        return entries.get(slot);

    }

    public void close(Player player) {

        openedCollections.remove(
                player.getUniqueId()
        );

    }

    private void buildNavigation(
            Inventory inventory,
            Player player) {

        ItemStack background =
                createBackgroundItem();

        for (int slot = 45; slot <= 53; slot++) {

            inventory.setItem(
                    slot,
                    background
            );

        }

        inventory.setItem(
                49,
                statisticsBuilder.build(player)
        );

        inventory.setItem(
                51,
                createMainMenuButton()
        );

    }

    private ItemStack createBackgroundItem() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.collection.background";

        String materialName =
                config.getString(
                        path + ".material",
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
                        " "
                );

        meta.setDisplayName(
                ColorUtil.color(name)
        );

        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createMainMenuButton() {

        FileConfiguration config =
                configManager.getGuiConfig();

        String path =
                "gui.collection.buttons.main-menu";

        String materialName =
                config.getString(
                        path + ".material",
                        "BARRIER"
                );

        Material material =
                Material.matchMaterial(
                        materialName
                );

        if (material == null) {

            material =
                    Material.BARRIER;

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
                        "&e← Ana Menü"
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