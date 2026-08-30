package com.sythel.fishera.scheduler;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.session.FishingSession;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public class FishingTask {

    public FishingTask(
            JavaPlugin plugin,
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry,
            FishingSession session,
            Runnable onBite) {

        ItemStack rodItem =
                session.getPlayer()
                        .getInventory()
                        .getItemInMainHand();

        int minDelay = 40;

        int maxDelay = 100;

        if (ItemUtil.isRod(rodItem)) {

            String rodId =
                    ItemUtil.getRodId(
                            rodItem
                    );

            RodData rod =
                    rodRegistry.get(
                            rodId
                    );

            if (rod != null) {

                minDelay =
                        rod.getMinWait() * 20;

                maxDelay =
                        rod.getMaxWait() * 20;

                int effectiveSpeed =
                        getEffectiveSpeed(
                                rod,
                                rodItem,
                                baitRegistry
                        );

                int speedBonus =
                        effectiveSpeed * 2;

                minDelay =
                        Math.max(
                                20,
                                minDelay - speedBonus
                        );

                maxDelay =
                        Math.max(
                                minDelay,
                                maxDelay - speedBonus
                        );

            }

        }

        int delay =
                ThreadLocalRandom.current()
                        .nextInt(
                                minDelay,
                                maxDelay + 1
                        );

        BukkitTask task =
                Bukkit.getScheduler()
                        .runTaskLater(
                                plugin,
                                () -> {

                                    if (!session
                                            .getPlayer()
                                            .isOnline()) {

                                        return;

                                    }

                                    if (session.getState()
                                            != FishingSession.State.WAITING) {

                                        return;

                                    }

                                    if (onBite != null) {

                                        onBite.run();

                                    }

                                },
                                delay
                        );

        session.setWaitingTask(
                task
        );

    }

    private int getEffectiveSpeed(
            RodData rod,
            ItemStack rodItem,
            BaitRegistry baitRegistry) {

        int speed =
                rod.getSpeed();

        if (!ItemUtil.isRod(rodItem)) {
            return speed;
        }

        String baitId =
                ItemUtil.getBaitId(
                        rodItem
                );

        if (baitId == null) {
            return speed;
        }

        BaitData bait =
                baitRegistry.get(
                        baitId
                );

        if (bait == null) {
            return speed;
        }

        return speed
                + bait.getSpeed();

    }

}