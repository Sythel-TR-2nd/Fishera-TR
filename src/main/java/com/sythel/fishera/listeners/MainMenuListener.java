package com.sythel.fishera.listeners;

import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.bait.BaitMenu;
import com.sythel.fishera.gui.rod.RodMenu;
import com.sythel.fishera.gui.sell.SellMenu;
import com.sythel.fishera.gui.task.TaskMenu;
import com.sythel.fishera.gui.top.TopListMenu;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class MainMenuListener implements Listener {

    private final MainMenu mainMenu;
    private final CollectionMenu collectionMenu;
    private final SellMenu sellMenu;
    private final RodMenu rodMenu;
    private final TopListMenu topListMenu;
    private final TaskMenu taskMenu;
    private final BaitMenu baitMenu;
    private final ConfigManager configManager;

    public MainMenuListener(
            MainMenu mainMenu,
            CollectionMenu collectionMenu,
            SellMenu sellMenu,
            RodMenu rodMenu,
            TopListMenu topListMenu,
            TaskMenu taskMenu,
            BaitMenu baitMenu,
            ConfigManager configManager) {

        this.mainMenu = mainMenu;
        this.collectionMenu = collectionMenu;
        this.sellMenu = sellMenu;
        this.rodMenu = rodMenu;
        this.topListMenu = topListMenu;
        this.taskMenu = taskMenu;
        this.baitMenu = baitMenu;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        String title =
                configManager.getGuiConfig().getString(
                        "gui.main.title",
                        "Fishera Balıkçılık"
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

            case FISHING_ROD ->

                    rodMenu.open(player);

            case WHEAT_SEEDS ->

                    baitMenu.open(player);

            case GOLD_INGOT ->

                    sellMenu.open(player);

            case WRITTEN_BOOK ->

                    collectionMenu.open(player);

            case PLAYER_HEAD ->

                    topListMenu.open(
                            player,
                            null,
                            List.of()
                    );

            case MAP ->

                    taskMenu.open(player);

            default -> {
            }
        }
    }
}