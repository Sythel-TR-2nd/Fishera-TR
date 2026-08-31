package com.sythel.fishera.service;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.builder.FishItemBuilder;
import com.sythel.fishera.fish.CaughtFish;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.repository.FishRepository;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FishingRewardService {

    private final FishSelector fishSelector;

    private final CaughtFishFactory caughtFishFactory;

    private final FishItemBuilder fishItemBuilder;

    private final FishRepository fishRepository;

    private final RodRegistry rodRegistry;

    private final TaskService taskService;

    private final BaitRegistry baitRegistry;

    private final MessageService messageService;

    public FishingRewardService(
            FishSelector fishSelector,
            CaughtFishFactory caughtFishFactory,
            FishItemBuilder fishItemBuilder,
            FishRepository fishRepository,
            RodRegistry rodRegistry,
            TaskService taskService,
            BaitRegistry baitRegistry,
            MessageService messageService) {

        this.fishSelector = fishSelector;

        this.caughtFishFactory = caughtFishFactory;

        this.fishItemBuilder = fishItemBuilder;

        this.fishRepository = fishRepository;

        this.rodRegistry = rodRegistry;

        this.taskService = taskService;

        this.baitRegistry = baitRegistry;

        this.messageService = messageService;

    }

    public CaughtFish reward(
            Player player) {

        ItemStack rodItem =
                player.getInventory()
                        .getItemInMainHand();

        if (!ItemUtil.isRod(rodItem)) {

            messageService.send(
                    player,
                    "fishing.invalid-rod"
            );

            return null;

        }

        String rodId =
                ItemUtil.getRodId(
                        rodItem
                );

        RodData rod =
                rodRegistry.get(
                        rodId
                );

        if (rod == null) {

            messageService.send(
                    player,
                    "fishing.invalid-rod-type"
            );

            return null;

        }

        FishData fish =
                fishSelector.getRandomFish(
                        rod,
                        rodItem
                );

        if (fish == null) {

            messageService.send(
                    player,
                    "fishing.no-fish"
            );

            return null;

        }

        CaughtFish caughtFish =
                caughtFishFactory.create(
                        fish,
                        rod,
                        rodItem
                );

        if (caughtFish == null) {

            player.sendMessage("");

            messageService.send(
                    player,
                    "fishing.too-heavy"
            );

            messageService.send(
                    player,
                    "fishing.too-heavy-detail"
            );

            messageService.send(
                    player,
                    "fishing.max-weight",
                    "weight",
                    String.format(
                            "%.2f",
                            rod.getMaxWeight()
                    )
            );

            player.sendMessage("");

            return null;

        }

        ItemStack fishItem =
                fishItemBuilder.build(
                        caughtFish
                );

        player.getInventory().addItem(
                fishItem
        );

        fishRepository.save(
                caughtFish,
                player.getUniqueId().toString(),
                player.getName()
        );

        consumeBait(
                player
        );

        taskService.handleFishCaught(
                player,
                fish.getId()
        );

        messageService.send(
                player,
                "fishing.caught",
                "fish",
                fish.getName()
        );

        return caughtFish;

    }

    private void consumeBait(
            Player player) {

        ItemStack rod =
                player.getInventory()
                        .getItemInMainHand();

        if (!ItemUtil.isRod(rod)) {
            return;
        }

        String baitId =
                ItemUtil.getBaitIdFromRod(
                        rod
                );

        if (baitId == null) {
            return;
        }

        BaitData bait =
                baitRegistry.get(
                        baitId
                );

        if (bait == null) {
            return;
        }

        int currentAmount =
                ItemUtil.getBaitAmount(
                        rod
                );

        if (currentAmount <= 0) {
            return;
        }

        int newAmount =
                currentAmount - 1;

        if (newAmount <= 0) {

            ItemUtil.removeBait(
                    rod
            );

            removeBaitLore(
                    rod
            );

        } else {

            ItemUtil.setBait(
                    rod,
                    baitId,
                    newAmount
            );

            updateBaitLore(
                    rod,
                    bait,
                    newAmount
            );

        }

        player.getInventory()
                .setItemInMainHand(
                        rod
                );

    }

    private void updateBaitLore(
            ItemStack rod,
            BaitData bait,
            int amount) {

        ItemMeta meta =
                rod.getItemMeta();

        if (meta == null) {
            return;
        }

        List<String> lore =
                meta.getLore() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(
                        meta.getLore()
                );

        removeBaitLoreLines(
                lore
        );

        removeTrailingEmptyLines(
                lore
        );

        lore.add("");

        lore.add(
                ColorUtil.color(
                        "&7Yem: "
                                + bait.getName()
                                + " &8("
                                + amount
                                + "/"
                                + bait.getMaxAmount()
                                + ")"
                )
        );

        if (bait.getWeight() != 0) {

            lore.add(
                    "§7⚖ Ağırlık Bonusu: §f+"
                            + bait.getWeight()
            );

        }

        if (bait.getSpeed() != 0) {

            lore.add(
                    "§7⚡ Hız Bonusu: §f+"
                            + bait.getSpeed()
            );

        }

        if (bait.getLuck() != 0) {

            lore.add(
                    "§7☘ Şans Bonusu: §f+"
                            + bait.getLuck()
            );

        }

        meta.setLore(
                lore
        );

        rod.setItemMeta(
                meta
        );

    }

    private void removeBaitLore(
            ItemStack rod) {

        ItemMeta meta =
                rod.getItemMeta();

        if (meta == null) {
            return;
        }

        if (meta.getLore() == null) {
            return;
        }

        List<String> lore =
                new ArrayList<>(
                        meta.getLore()
                );

        removeBaitLoreLines(
                lore
        );

        removeTrailingEmptyLines(
                lore
        );

        meta.setLore(
                lore
        );

        rod.setItemMeta(
                meta
        );

    }

    private void removeBaitLoreLines(
            List<String> lore) {

        lore.removeIf(
                line ->
                        line.contains("Yem:")
                                || line.contains(
                                "⚖ Ağırlık Bonusu:"
                        )
                                || line.contains(
                                "⚡ Hız Bonusu:"
                        )
                                || line.contains(
                                "☘ Şans Bonusu:"
                        )
        );

    }

    private void removeTrailingEmptyLines(
            List<String> lore) {

        while (!lore.isEmpty()) {

            String last =
                    lore.get(
                            lore.size() - 1
                    );

            if (last == null
                    || last.isEmpty()
                    || last.equals(" ")) {

                lore.remove(
                        lore.size() - 1
                );

            } else {

                break;

            }

        }

    }

}
