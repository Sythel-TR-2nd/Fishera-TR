package com.sythel.fishera.simulation;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.rod.RodData;
import com.sythel.fishera.util.PriceUtil;

public class SimulationEngine {

    private final FishRegistry fishRegistry;

    public SimulationEngine(FishRegistry fishRegistry) {
        this.fishRegistry = fishRegistry;
    }

    public SimulationResult calculate(
            RodData rod,
            BaitData bait,
            int durationSeconds
    ) {
        if (rod == null || durationSeconds <= 0) {
            return new SimulationResult(
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );
        }

        double baitSpeed =
                bait != null
                        ? bait.getSpeed()
                        : 0.0;

        double baitLuck =
                bait != null
                        ? bait.getLuck()
                        : 0.0;

        int baitWeight =
                bait != null
                        ? bait.getWeight()
                        : 0;

        double baitPrice =
                bait != null
                        ? bait.getPrice()
                        : 0.0;

        double speedBonus =
                (rod.getSpeed() + baitSpeed) * 2.0;

        double minWait =
                Math.max(
                        20.0,
                        rod.getMinWait() * 20.0 - speedBonus
                );

        double maxWait =
                Math.max(
                        minWait,
                        rod.getMaxWait() * 20.0 - speedBonus
                );

        double averageWaitTicks =
                (minWait + maxWait) / 2.0;

        double averageWaitSeconds =
                averageWaitTicks / 20.0;

        double attemptsPerHour =
                averageWaitSeconds > 0.0
                        ? 3600.0 / averageWaitSeconds
                        : 0.0;

        ExpectedFishResult expectedFish =
                calculateExpectedFish(
                        rod,
                        baitLuck,
                        baitWeight
                );

        double catchesPerHour =
                attemptsPerHour
                        * expectedFish.successProbability;

        double grossIncomePerHour =
                attemptsPerHour
                        * expectedFish.valuePerAttempt;

        double baitCostPerHour =
                catchesPerHour * baitPrice;

        double netIncomePerHour =
                grossIncomePerHour - baitCostPerHour;

        double durationMultiplier =
                durationSeconds / 3600.0;

        double actualCatches =
                catchesPerHour * durationMultiplier;

        double durationGrossIncome =
                grossIncomePerHour * durationMultiplier;

        double durationBaitCost =
                baitCostPerHour * durationMultiplier;

        double durationNetIncome =
                netIncomePerHour * durationMultiplier;

        double averageFishValue =
                catchesPerHour > 0.0
                        ? grossIncomePerHour / catchesPerHour
                        : 0.0;

        return new SimulationResult(
                actualCatches,
                averageFishValue,
                durationGrossIncome,
                durationBaitCost,
                durationNetIncome
        );
    }

    private ExpectedFishResult calculateExpectedFish(
            RodData rod,
            double baitLuck,
            int baitWeight
    ) {
        double totalChance = 0.0;

        for (FishData fish : fishRegistry.getAll()) {

            if (!canCatch(rod, fish)) {
                continue;
            }

            double modifiedChance =
                    calculateModifiedChance(
                            fish,
                            rod.getLuck() + baitLuck
                    );

            if (modifiedChance > 0.0) {
                totalChance += modifiedChance;
            }
        }

        if (totalChance <= 0.0) {
            return new ExpectedFishResult(
                    0.0,
                    0.0
            );
        }

        double successProbability = 0.0;
        double valuePerAttempt = 0.0;

        for (FishData fish : fishRegistry.getAll()) {

            if (!canCatch(rod, fish)) {
                continue;
            }

            double modifiedChance =
                    calculateModifiedChance(
                            fish,
                            rod.getLuck() + baitLuck
                    );

            if (modifiedChance <= 0.0) {
                continue;
            }

            double selectionProbability =
                    modifiedChance / totalChance;

            FishValue fishValue =
                    calculateFishValue(
                            rod,
                            fish,
                            baitWeight
                    );

            successProbability +=
                    selectionProbability
                            * fishValue.catchProbability;

            valuePerAttempt +=
                    selectionProbability
                            * fishValue.catchProbability
                            * fishValue.averagePrice;
        }

        return new ExpectedFishResult(
                successProbability,
                valuePerAttempt
        );
    }

    private boolean canCatch(
            RodData rod,
            FishData fish
    ) {
        return getRarityOrder(
                fish.getRarity()
        ) <= getRarityOrder(
                rod.getMaxRarity()
        );
    }

    private int getRarityOrder(
            String rarity
    ) {
        if (rarity == null) {
            return -1;
        }

        return switch (rarity.toUpperCase()) {
            case "COMMON" -> 0;
            case "UNCOMMON" -> 1;
            case "RARE" -> 2;
            case "EPIC" -> 3;
            case "LEGENDARY" -> 4;
            default -> -1;
        };
    }

    private double calculateModifiedChance(
            FishData fish,
            double luck
    ) {
        double multiplier = 1.0;

        String rarity =
                fish.getRarity();

        if (rarity != null) {
            switch (rarity.toUpperCase()) {
                case "COMMON" ->
                        multiplier -= luck * 0.0010;

                case "UNCOMMON" ->
                        multiplier -= luck * 0.0005;

                case "RARE" ->
                        multiplier += luck * 0.0005;

                case "EPIC" ->
                        multiplier += luck * 0.0015;

                case "LEGENDARY" ->
                        multiplier += luck * 0.0025;

                default -> {
                }
            }
        }

        multiplier =
                Math.max(
                        0.10,
                        multiplier
                );

        return Math.max(
                1.0,
                Math.round(
                        fish.getChance() * multiplier
                )
        );
    }

    private FishValue calculateFishValue(
            RodData rod,
            FishData fish,
            int baitWeight
    ) {
        double minWeight =
                fish.getMinWeight();

        double maxWeight =
                fish.getMaxWeight();

        double rodMaxWeight =
                rod.getMaxWeight();

        if (minWeight > rodMaxWeight) {
            return new FishValue(
                    0.0,
                    0.0
            );
        }

        if (minWeight == maxWeight) {
            double price =
                    PriceUtil.calculate(
                            fish.getBasePrice(),
                            minWeight,
                            rod.getSell()
                    );

            return new FishValue(
                    1.0,
                    price
            );
        }

        double range =
                maxWeight - minWeight;

        double weightBonus =
                Math.min(
                        (rod.getWeight() + baitWeight)
                                / 2000.0,
                        0.35
                );

        double threshold =
                (rodMaxWeight - minWeight)
                        / range;

        if (threshold <= weightBonus) {
            return new FishValue(
                    0.0,
                    0.0
            );
        }

        if (threshold >= 1.0) {
            double averageRandom =
                    weightBonus
                            + ((1.0 - weightBonus)
                            * (1.0 - weightBonus)
                            / 2.0);

            double averageWeight =
                    minWeight
                            + range * averageRandom;

            averageWeight =
                    Math.round(
                            averageWeight * 100.0
                    ) / 100.0;

            double price =
                    PriceUtil.calculate(
                            fish.getBasePrice(),
                            averageWeight,
                            rod.getSell()
                    );

            return new FishValue(
                    1.0,
                    price
            );
        }

        double catchProbability =
                threshold - weightBonus;

        double averageRandom =
                (weightBonus + threshold) / 2.0;

        double averageWeight =
                minWeight
                        + range * averageRandom;

        averageWeight =
                Math.round(
                        averageWeight * 100.0
                ) / 100.0;

        double price =
                PriceUtil.calculate(
                        fish.getBasePrice(),
                        averageWeight,
                        rod.getSell()
                );

        return new FishValue(
                catchProbability,
                price
        );
    }

    private static class ExpectedFishResult {

        private final double successProbability;
        private final double valuePerAttempt;

        private ExpectedFishResult(
                double successProbability,
                double valuePerAttempt
        ) {
            this.successProbability =
                    successProbability;

            this.valuePerAttempt =
                    valuePerAttempt;
        }
    }

    private static class FishValue {

        private final double catchProbability;
        private final double averagePrice;

        private FishValue(
                double catchProbability,
                double averagePrice
        ) {
            this.catchProbability =
                    catchProbability;

            this.averagePrice =
                    averagePrice;
        }
    }
}