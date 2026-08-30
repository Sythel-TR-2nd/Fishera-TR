package com.sythel.fishera.listeners;

import com.sythel.fishera.collection.CollectionEntry;
import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.collection.detail.FishDetailMenu;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CollectionListener implements Listener {

    private final CollectionMenu collectionMenu;

    private final FishDetailMenu detailMenu;

    private final MainMenu mainMenu;

    private final MessageService messageService;

    private final ConfigManager configManager;

    public CollectionListener(
            CollectionMenu collectionMenu,
            FishDetailMenu detailMenu,
            MainMenu mainMenu,
            MessageService messageService,
            ConfigManager configManager) {

        this.collectionMenu = collectionMenu;

        this.detailMenu = detailMenu;

        this.mainMenu = mainMenu;

        this.messageService = messageService;

        this.configManager = configManager;

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.collection.title",
                        "Balık Koleksiyonu"
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

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getType()
                == Material.AIR) {

            return;
        }

        int slot =
                event.getRawSlot();

        if (slot < 0) {
            return;
        }

        if (slot >= event.getView()
                .getTopInventory()
                .getSize()) {

            return;
        }

        Material material =
                event.getCurrentItem()
                        .getType();

        switch (material) {

            case BARRIER -> {

                mainMenu.open(player);

            }

            case ARROW -> {

                return;

            }

            default -> {

                CollectionEntry entry =
                        collectionMenu.getEntry(
                                player,
                                slot
                        );

                if (entry == null) {
                    return;
                }

                if (!entry.isCaught()) {

                    messageService.send(
                            player,
                            "general.fish-not-discovered"
                    );

                    return;
                }

                detailMenu.open(
                        player,
                        entry
                );

            }

        }

    }

}