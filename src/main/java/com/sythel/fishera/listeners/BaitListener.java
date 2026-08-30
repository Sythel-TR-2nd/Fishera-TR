package com.sythel.fishera.listeners;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.service.MessageService;
import com.sythel.fishera.util.ColorUtil;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BaitListener implements Listener {

    private final BaitRegistry baitRegistry;

    private final MessageService messageService;

    private final NamespacedKey baitIdKey;

    private final NamespacedKey baitAmountKey;

    public BaitListener(
            JavaPlugin plugin,
            BaitRegistry baitRegistry,
            MessageService messageService) {

        this.baitRegistry = baitRegistry;

        this.messageService = messageService;

        this.baitIdKey =
                new NamespacedKey(
                        plugin,
                        "bait_id"
                );

        this.baitAmountKey =
                new NamespacedKey(
                        plugin,
                        "bait_amount"
                );

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(
            InventoryClickEvent event) {

        if (!(event.getWhoClicked()
                instanceof Player player)) {

            return;
        }

        ItemStack cursor =
                event.getCursor();

        ItemStack clicked =
                event.getCurrentItem();

        if (!ItemUtil.isBait(cursor)) {
            return;
        }

        if (!ItemUtil.isRod(clicked)) {
            return;
        }

        BaitData bait =
                baitRegistry.get(
                        ItemUtil.getBaitId(cursor)
                );

        if (bait == null) {
            return;
        }

        event.setCancelled(true);

        int cursorAmount =
                cursor.getAmount();

        if (cursorAmount <= 0) {
            return;
        }

        String currentBaitId =
                getBaitId(clicked);

        int currentAmount =
                getBaitAmount(clicked);

        if (currentBaitId != null
                && !currentBaitId.equalsIgnoreCase(
                bait.getId()
        )) {

            messageService.send(
                    player,
                    "bait.already-equipped"
            );

            return;
        }

        int remainingCapacity =
                bait.getMaxAmount()
                        - currentAmount;

        if (remainingCapacity <= 0) {

            messageService.send(
                    player,
                    "bait.capacity-full"
            );

            return;
        }

        int addedAmount =
                Math.min(
                        cursorAmount,
                        remainingCapacity
                );

        int newAmount =
                currentAmount + addedAmount;

        setBait(
                clicked,
                bait.getId(),
                newAmount
        );

        updateRodLore(
                clicked,
                bait,
                newAmount
        );

        int remainingCursorAmount =
                cursorAmount - addedAmount;

        if (remainingCursorAmount <= 0) {

            event.getView()
                    .setCursor(null);

        } else {

            cursor.setAmount(
                    remainingCursorAmount
            );

            event.getView()
                    .setCursor(cursor);

        }

        event.setCurrentItem(
                clicked
        );

        messageService.send(
                player,
                "bait.equipped",
                "amount",
                String.valueOf(addedAmount),
                "bait",
                bait.getName()
        );

    }

    private String getBaitId(
            ItemStack rod) {

        if (!ItemUtil.isRod(rod)) {
            return null;
        }

        ItemMeta meta =
                rod.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer()
                .get(
                        baitIdKey,
                        PersistentDataType.STRING
                );

    }

    private int getBaitAmount(
            ItemStack rod) {

        if (!ItemUtil.isRod(rod)) {
            return 0;
        }

        ItemMeta meta =
                rod.getItemMeta();

        if (meta == null) {
            return 0;
        }

        Integer amount =
                meta.getPersistentDataContainer()
                        .get(
                                baitAmountKey,
                                PersistentDataType.INTEGER
                        );

        return amount == null
                ? 0
                : amount;

    }

    private void setBait(
            ItemStack rod,
            String baitId,
            int amount) {

        ItemMeta meta =
                rod.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer()
                .set(
                        baitIdKey,
                        PersistentDataType.STRING,
                        baitId
                );

        meta.getPersistentDataContainer()
                .set(
                        baitAmountKey,
                        PersistentDataType.INTEGER,
                        amount
                );

        rod.setItemMeta(
                meta
        );

    }

    private void updateRodLore(
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

        while (!lore.isEmpty()
                && lore.get(
                lore.size() - 1
        ).isEmpty()) {

            lore.remove(
                    lore.size() - 1
            );

        }

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
                    ColorUtil.color(
                            "&7⚖ Ağırlık Bonusu: &f+"
                                    + bait.getWeight()
                    )
            );

        }

        if (bait.getSpeed() != 0) {

            lore.add(
                    ColorUtil.color(
                            "&7⚡ Hız Bonusu: &f+"
                                    + bait.getSpeed()
                    )
            );

        }

        if (bait.getLuck() != 0) {

            lore.add(
                    ColorUtil.color(
                            "&7☘ Şans Bonusu: &f+"
                                    + bait.getLuck()
                    )
            );

        }

        meta.setLore(
                lore
        );

        rod.setItemMeta(
                meta
        );

    }

}