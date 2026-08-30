package com.sythel.fishera.service;

import com.sythel.fishera.util.ItemUtil;
import com.sythel.fishera.util.NumberUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellService {

    private final EconomyService economyService;

    private final MessageService messageService;

    public SellService(
            EconomyService economyService,
            MessageService messageService) {

        this.economyService = economyService;

        this.messageService = messageService;

    }

    public double getInventoryValue(Player player) {

        double total = 0;

        for (ItemStack item :
                player.getInventory().getContents()) {

            if (!ItemUtil.isFish(item)) {
                continue;
            }

            total +=
                    ItemUtil.getFishPrice(item)
                            * item.getAmount();

        }

        return total;

    }

    public int getFishAmount(Player player) {

        int amount = 0;

        for (ItemStack item :
                player.getInventory().getContents()) {

            if (!ItemUtil.isFish(item)) {
                continue;
            }

            amount += item.getAmount();

        }

        return amount;

    }

    public boolean sellInventory(Player player) {

        double totalMoney = 0;

        int soldFish = 0;

        for (int slot = 0;
             slot < player.getInventory().getSize();
             slot++) {

            ItemStack item =
                    player.getInventory().getItem(slot);

            if (!ItemUtil.isFish(item)) {
                continue;
            }

            totalMoney +=
                    ItemUtil.getFishPrice(item)
                            * item.getAmount();

            soldFish += item.getAmount();

            player.getInventory().setItem(
                    slot,
                    null
            );

        }

        if (soldFish == 0) {

            messageService.send(
                    player,
                    "sell.no-fish"
            );

            return false;

        }

        economyService.deposit(
                player,
                totalMoney
        );

        player.sendMessage("");

        messageService.send(
                player,
                "general.separator"
        );

        messageService.send(
                player,
                "sell.title"
        );

        messageService.send(
                player,
                "general.separator"
        );

        player.sendMessage("");

        messageService.send(
                player,
                "sell.sold-fish",
                "amount",
                String.valueOf(soldFish)
        );

        messageService.send(
                player,
                "sell.earned-money",
                "money",
                NumberUtil.formatMoney(totalMoney)
        );

        player.sendMessage("");

        player.playSound(
                player.getLocation(),
                Sound.ENTITY_PLAYER_LEVELUP,
                1F,
                1F
        );

        return true;

    }

}