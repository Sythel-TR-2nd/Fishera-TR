package com.sythel.fishera.fish;

import java.util.List;

public class FishData {

    private final String id;
    private final String name;
    private final String rarity;

    private final int chance;

    private final double minWeight;
    private final double maxWeight;

    private final double basePrice;

    private final String material;
    private final int customModelData;

    private final List<String> lore;

    private final List<String> hint;

    public FishData(String id,
                    String name,
                    String rarity,
                    int chance,
                    double minWeight,
                    double maxWeight,
                    double basePrice,
                    String material,
                    int customModelData,
                    List<String> lore,
                    List<String> hint) {

        this.id = id;
        this.name = name;
        this.rarity = rarity;

        this.chance = chance;

        this.minWeight = minWeight;
        this.maxWeight = maxWeight;

        this.basePrice = basePrice;

        this.material = material;
        this.customModelData = customModelData;

        this.lore = lore;
        this.hint = hint;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public int getChance() {
        return chance;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getHint() {
        return hint;
    }

}