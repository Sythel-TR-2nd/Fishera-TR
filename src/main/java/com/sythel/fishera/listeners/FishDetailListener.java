package com.sythel.fishera.listeners;

import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.collection.detail.FishDetailMenu;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FishDetailListener implements Listener {

    private final CollectionMenu collectionMenu;
    private final FishDetailMenu detailMenu;
    private final ConfigManager configManager;

    public FishDetailListener(
            CollectionMenu collectionMenu,
            FishDetailMenu detailMenu,
            ConfigManager configManager) {

        this.collectionMenu = collectionMenu;
        this.detailMenu = detailMenu;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        String title =
                configManager.getGuiConfig().getString(
                        "gui.fish-detail.title",
                        "Fishera • Balık Detayları"
                );

        title =
                ColorUtil.color(title);

        if (!event.getView()
                .getTitle()
                .equals(title)) {

            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked()
                instanceof Player player)) {

            return;
        }

        if (event.getRawSlot() < 0
                || event.getRawSlot()
                >= event.getView()
                .getTopInventory()
                .getSize()) {

            return;
        }

        if (event.getCurrentItem() == null
                || event.getCurrentItem()
                .getType() == Material.AIR) {

            return;
        }

        switch (event.getCurrentItem().getType()) {

            case ARROW ->

                    collectionMenu.open(player);

            case BARRIER ->

                    player.closeInventory();

            default -> {
            }
        }
    }
}