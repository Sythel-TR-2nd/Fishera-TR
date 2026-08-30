package com.sythel.fishera.fish;

import com.sythel.fishera.rarity.RarityData;

public class CaughtFish {

    private final FishData fishData;

    private final RarityData rarityData;

    private final double weight;

    private final double price;

    public CaughtFish(FishData fishData,
                      RarityData rarityData,
                      double weight,
                      double price) {

        this.fishData = fishData;
        this.rarityData = rarityData;
        this.weight = weight;
        this.price = price;

    }

    public FishData getFishData() {

        return fishData;

    }

    public RarityData getRarityData() {

        return rarityData;

    }

    public double getWeight() {

        return weight;

    }

    public double getPrice() {

        return price;

    }

}