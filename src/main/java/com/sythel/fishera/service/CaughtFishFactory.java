package com.sythel.fishera.service;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.fish.CaughtFish;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.rarity.RarityData;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.RarityRegistry;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.ItemUtil;
import com.sythel.fishera.util.PriceUtil;
import com.sythel.fishera.util.WeightUtil;
import org.bukkit.inventory.ItemStack;

public class CaughtFishFactory {

    private final RarityRegistry rarityRegistry;

    private final BaitRegistry baitRegistry;

    public CaughtFishFactory(
            RarityRegistry rarityRegistry,
            BaitRegistry baitRegistry) {

        this.rarityRegistry = rarityRegistry;

        this.baitRegistry = baitRegistry;

    }

    public CaughtFish create(
            FishData fishData,
            RodData rod,
            ItemStack rodItem) {

        RarityData rarity =
                rarityRegistry.get(
                        fishData.getRarity()
                );

        int effectiveWeight =
                getEffectiveWeight(
                        rod,
                        rodItem
                );

        double weight =
                WeightUtil.generate(
                        fishData.getMinWeight(),
                        fishData.getMaxWeight(),
                        effectiveWeight
                );

        if (weight > rod.getMaxWeight()) {
            return null;
        }

        double price =
                PriceUtil.calculate(
                        fishData.getBasePrice(),
                        weight,
                        rod.getSell()
                );

        return new CaughtFish(
                fishData,
                rarity,
                weight,
                price
        );

    }

    private int getEffectiveWeight(
            RodData rod,
            ItemStack rodItem) {

        int weight =
                rod.getWeight();

        if (!ItemUtil.isRod(rodItem)) {
            return weight;
        }

        String baitId =
                ItemUtil.getBaitId(
                        rodItem
                );

        if (baitId == null) {
            return weight;
        }

        BaitData bait =
                baitRegistry.get(
                        baitId
                );

        if (bait == null) {
            return weight;
        }

        return weight
                + bait.getWeight();

    }

}