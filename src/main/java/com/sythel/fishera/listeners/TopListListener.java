package com.sythel.fishera.listeners;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.top.TopListMenu;
import com.sythel.fishera.top.TopCategory;
import com.sythel.fishera.top.TopListService;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TopListListener implements Listener {

    private final TopListMenu topListMenu;
    private final TopListService topListService;
    private final MainMenu mainMenu;
    private final ConfigManager configManager;

    public TopListListener(
            TopListMenu topListMenu,
            TopListService topListService,
            MainMenu mainMenu,
            ConfigManager configManager) {

        this.topListMenu = topListMenu;
        this.topListService = topListService;
        this.mainMenu = mainMenu;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.top.title",
                        "Fishera • Sıralama"
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

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem()
                .getType() == Material.AIR) {

            return;
        }

        switch (event.getCurrentItem().getType()) {

            case GOLD_INGOT -> {

                topListMenu.open(
                        player,
                        TopCategory.MONEY,
                        topListService.getTopMoney()
                );

            }

            case COD -> {

                topListMenu.open(
                        player,
                        TopCategory.FISH,
                        topListService.getTopFishCount()
                );

            }

            case NAUTILUS_SHELL -> {

                topListMenu.open(
                        player,
                        TopCategory.WEIGHT,
                        topListService.getTopWeight()
                );

            }

            case WRITTEN_BOOK -> {

                topListMenu.open(
                        player,
                        TopCategory.COLLECTION,
                        topListService.getTopCollection()
                );

            }

            case BARRIER -> {

                mainMenu.open(
                        player
                );

            }

            default -> {
            }
        }
    }
}