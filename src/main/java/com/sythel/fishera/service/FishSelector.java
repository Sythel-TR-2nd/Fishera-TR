package com.sythel.fishera.service;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.rarity.RarityType;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class FishSelector {

    private final FishRegistry fishRegistry;

    private final BaitRegistry baitRegistry;

    public FishSelector(FishRegistry fishRegistry,
                        BaitRegistry baitRegistry) {

        this.fishRegistry = fishRegistry;

        this.baitRegistry = baitRegistry;

    }

    public FishData getRandomFish(RodData rod,
                                  ItemStack rodItem) {

        List<FishData> fishes =
                new ArrayList<>();

        for (FishData fish :
                fishRegistry.getAll()) {

            if (canCatch(
                    rod,
                    fish
            )) {

                fishes.add(fish);

            }

        }

        if (fishes.isEmpty()) {
            return null;
        }

        int totalChance = 0;

        for (FishData fish : fishes) {

            totalChance += getModifiedChance(
                    fish,
                    rod,
                    rodItem
            );

        }

        if (totalChance <= 0) {
            return null;
        }

        int random =
                ThreadLocalRandom.current()
                        .nextInt(
                                totalChance
                        );

        int current = 0;

        for (FishData fish : fishes) {

            current += getModifiedChance(
                    fish,
                    rod,
                    rodItem
            );

            if (random < current) {
                return fish;
            }

        }

        return fishes.get(
                fishes.size() - 1
        );

    }

    private int getModifiedChance(
            FishData fish,
            RodData rod,
            ItemStack rodItem) {

        double multiplier = 1.0;

        int luck =
                getEffectiveLuck(
                        rod,
                        rodItem
                );

        RarityType rarity =
                RarityType.valueOf(
                        fish.getRarity()
                                .trim()
                                .toUpperCase(
                                        Locale.ROOT
                                )
                );

        switch (rarity) {

            case COMMON ->

                    multiplier -=
                            luck * 0.0010;

            case UNCOMMON ->

                    multiplier -=
                            luck * 0.0005;

            case RARE ->

                    multiplier +=
                            luck * 0.0005;

            case EPIC ->

                    multiplier +=
                            luck * 0.0015;

            case LEGENDARY ->

                    multiplier +=
                            luck * 0.0025;

        }

        multiplier =
                Math.max(
                        0.10,
                        multiplier
                );

        return Math.max(
                1,
                (int) Math.round(
                        fish.getChance()
                                * multiplier
                )
        );

    }

    private int getEffectiveLuck(
            RodData rod,
            ItemStack rodItem) {

        int luck =
                rod.getLuck();

        if (!ItemUtil.isRod(rodItem)) {
            return luck;
        }

        String baitId =
                ItemUtil.getBaitId(
                        rodItem
                );

        if (baitId == null) {
            return luck;
        }

        BaitData bait =
                baitRegistry.get(
                        baitId
                );

        if (bait == null) {
            return luck;
        }

        return luck
                + bait.getLuck();

    }

    private boolean canCatch(
            RodData rod,
            FishData fish) {

        RarityType fishRarity =
                RarityType.valueOf(
                        fish.getRarity()
                                .trim()
                                .toUpperCase(
                                        Locale.ROOT
                                )
                );

        RarityType rodRarity =
                RarityType.valueOf(
                        rod.getMaxRarity()
                                .trim()
                                .toUpperCase(
                                        Locale.ROOT
                                )
                );

        return fishRarity.ordinal()
                <= rodRarity.ordinal();

    }

}