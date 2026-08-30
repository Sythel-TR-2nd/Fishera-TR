package com.sythel.fishera.listeners;

import com.sythel.fishera.builder.RodItemBuilder;
import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.gui.MainMenu;
import com.sythel.fishera.gui.rod.RodMenu;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.rod.RodData;
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

public class RodMenuListener implements Listener {

    private static final int RODS_PER_PAGE = 10;

    private final RodRegistry rodRegistry;
    private final RodItemBuilder rodItemBuilder;
    private final EconomyService economyService;
    private final MessageService messageService;
    private final RodMenu rodMenu;
    private final MainMenu mainMenu;
    private final ConfigManager configManager;

    private final Map<Player, Integer> pages =
            new HashMap<>();

    public RodMenuListener(
            RodRegistry rodRegistry,
            RodItemBuilder rodItemBuilder,
            EconomyService economyService,
            RodMenu rodMenu,
            MainMenu mainMenu,
            MessageService messageService,
            ConfigManager configManager) {

        this.rodRegistry = rodRegistry;
        this.rodItemBuilder = rodItemBuilder;
        this.economyService = economyService;
        this.messageService = messageService;
        this.rodMenu = rodMenu;
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
                        "gui.rod.title",
                        "Fishera • Oltalar"
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
                        (rodRegistry.getAll().size() - 1)
                                / RODS_PER_PAGE
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

                rodMenu.open(
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

                rodMenu.open(
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

            default -> {
            }
        }

        if (!ItemUtil.isRod(clicked)) {
            return;
        }

        String rodId =
                ItemUtil.getRodId(
                        clicked
                );

        RodData rod =
                rodRegistry.get(
                        rodId
                );

        if (rod == null) {
            return;
        }

        if (economyService.getBalance(
                player
        ) < rod.getPrice()) {

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
                rod.getPrice()
        );

        ItemStack rodItem =
                rodItemBuilder.build(
                        rod
                );

        Map<Integer, ItemStack> leftover =
                player.getInventory()
                        .addItem(
                                rodItem
                        );

        if (!leftover.isEmpty()) {

            economyService.deposit(
                    player,
                    rod.getPrice()
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
                        "rod.purchased",
                        "rod",
                        rod.getName()
                )
        );

        player.sendMessage(
                messageService.get(
                        "rod.price",
                        "price",
                        String.format(
                                "%,.2f",
                                rod.getPrice()
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