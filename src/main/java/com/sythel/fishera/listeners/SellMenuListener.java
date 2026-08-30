package com.sythel.fishera.listeners;

import com.sythel.fishera.gui.sell.SellMenu;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.service.SellService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SellMenuListener implements Listener {

    private final SellService sellService;
    private final MessageService messageService;
    private final SellMenu sellMenu;

    public SellMenuListener(
            SellService sellService,
            MessageService messageService,
            SellMenu sellMenu) {

        this.sellService = sellService;
        this.messageService = messageService;
        this.sellMenu = sellMenu;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        if (!event.getView()
                .getTitle()
                .equals(sellMenu.getTitle())) {

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

        if (event.getCurrentItem()
                .getType() == Material.AIR) {

            return;
        }

        switch (event.getCurrentItem().getType()) {

            case GOLD_INGOT -> {

                sellService.sellInventory(
                        player
                );

                player.closeInventory();

            }

            case BARRIER -> {

                player.closeInventory();

            }

            default -> {
            }
        }
    }
}