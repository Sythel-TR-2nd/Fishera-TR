package com.sythel.fishera.listeners;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.builder.BaitItemBuilder;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.bait.BaitMenu;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.service.EconomyService;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BaitMenuListener implements Listener {

    private static final int BAITS_PER_PAGE = 10;

    private final BaitRegistry baitRegistry;
    private final BaitItemBuilder baitItemBuilder;
    private final EconomyService economyService;
    private final MessageService messageService;
    private final BaitMenu baitMenu;
    private final MainMenu mainMenu;
    private final ConfigManager configManager;

    private final Map<Player, Integer> pages =
            new HashMap<>();

    public BaitMenuListener(
            BaitRegistry baitRegistry,
            BaitItemBuilder baitItemBuilder,
            EconomyService economyService,
            BaitMenu baitMenu,
            MainMenu mainMenu,
            MessageService messageService,
            ConfigManager configManager) {

        this.baitRegistry = baitRegistry;
        this.baitItemBuilder = baitItemBuilder;
        this.economyService = economyService;
        this.baitMenu = baitMenu;
        this.mainMenu = mainMenu;
        this.messageService = messageService;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(
            InventoryClickEvent event) {

        FileConfiguration config =
                configManager.getGuiConfig();

        String title =
                config.getString(
                        "gui.bait.title",
                        "Fishera • Yemler"
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

        ItemStack clicked =
                event.getCurrentItem();

        if (clicked == null) {
            return;
        }

        if (clicked.getType()
                == Material.AIR) {

            return;
        }

        int maxPage =
                Math.max(
                        0,
                        (baitRegistry.getAll().size() - 1)
                                / BAITS_PER_PAGE
                );

        switch (event.getSlot()) {

            case 27 -> {

                int page =
                        Math.max(
                                pages.getOrDefault(
                                        player,
                                        0
                                ) - 1,
                                0
                        );

                pages.put(
                        player,
                        page
                );

                baitMenu.open(
                        player,
                        page
                );

                return;
            }

            case 35 -> {

                int page =
                        Math.min(
                                pages.getOrDefault(
                                        player,
                                        0
                                ) + 1,
                                maxPage
                        );

                pages.put(
                        player,
                        page
                );

                baitMenu.open(
                        player,
                        page
                );

                return;
            }

            case 31 -> {

                pages.remove(player);

                mainMenu.open(
                        player
                );

                return;
            }
        }

        if (!ItemUtil.isBait(clicked)) {
            return;
        }

        String baitId =
                ItemUtil.getBaitId(
                        clicked
                );

        BaitData bait =
                baitRegistry.get(
                        baitId
                );

        if (bait == null) {
            return;
        }

        if (economyService.getBalance(
                player
        ) < bait.getPrice()) {

            messageService.send(
                    player,
                    "general.no-balance"
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO,
                    1F,
                    1F
            );

            return;
        }

        economyService.withdraw(
                player,
                bait.getPrice()
        );

        ItemStack baitItem =
                baitItemBuilder.build(
                        bait,
                        1
                );

        Map<Integer, ItemStack> leftover =
                player.getInventory()
                        .addItem(
                                baitItem
                        );

        if (!leftover.isEmpty()) {

            economyService.deposit(
                    player,
                    bait.getPrice()
            );

            messageService.send(
                    player,
                    "general.inventory-full"
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO,
                    1F,
                    1F
            );

            return;
        }

        player.sendMessage("");

        player.sendMessage(
                messageService.get(
                        "general.separator"
                )
        );

        player.sendMessage(
                messageService.get(
                        "general.purchase-success"
                )
        );

        player.sendMessage(
                messageService.get(
                        "bait.purchased",
                        "bait",
                        bait.getName()
                )
        );

        player.sendMessage(
                messageService.get(
                        "bait.price",
                        "price",
                        String.format(
                                "%,.2f",
                                bait.getPrice()
                        )
                )
        );

        player.sendMessage(
                messageService.get(
                        "general.separator"
                )
        );

        player.playSound(
                player.getLocation(),
                Sound.ENTITY_PLAYER_LEVELUP,
                1F,
                1F
        );
    }
}