package com.sythel.fishera.rod;

import java.util.List;

public class RodData {

    private final String id;

    private final String name;

    private final RodSeries series;

    private final String description;

    private final String material;

    private final int customModelData;

    private final double price;

    private final double maxWeight;

    private final String maxRarity;

    private final int minWait;

    private final int maxWait;

    private final int weight;

    private final int speed;

    private final int luck;

    private final int sell;

    private final double perfectChance;

    private final String passiveName;

    private final String passiveDescription;

    private final List<String> lore;

    public RodData(String id,
                   String name,
                   RodSeries series,
                   String description,
                   String material,
                   int customModelData,
                   double price,
                   double maxWeight,
                   String maxRarity,
                   int minWait,
                   int maxWait,
                   int weight,
                   int speed,
                   int luck,
                   int sell,
                   double perfectChance,
                   String passiveName,
                   String passiveDescription,
                   List<String> lore) {

        this.id = id;
        this.name = name;
        this.series = series;
        this.description = description;
        this.material = material;
        this.customModelData = customModelData;
        this.price = price;
        this.maxWeight = maxWeight;
        this.maxRarity = maxRarity;
        this.minWait = minWait;
        this.maxWait = maxWait;
        this.weight = weight;
        this.speed = speed;
        this.luck = luck;
        this.sell = sell;
        this.perfectChance = perfectChance;
        this.passiveName = passiveName;
        this.passiveDescription = passiveDescription;
        this.lore = lore;

    }

    public String getId() {

        return id;

    }

    public String getName() {

        return name;

    }

    public RodSeries getSeries() {

        return series;

    }

    public String getDescription() {

        return description;

    }

    public String getMaterial() {

        return material;

    }

    public int getCustomModelData() {

        return customModelData;

    }

    public double getPrice() {

        return price;

    }

    public double getMaxWeight() {

        return maxWeight;

    }

    public String getMaxRarity() {

        return maxRarity;

    }

    public int getMinWait() {

        return minWait;

    }

    public int getMaxWait() {

        return maxWait;

    }

    public int getWeight() {

        return weight;

    }

    public int getSpeed() {

        return speed;

    }

    public int getLuck() {

        return luck;

    }

    public int getSell() {

        return sell;

    }

    public double getPerfectChance() {

        return perfectChance;

    }

    public String getPassiveName() {

        return passiveName;

    }

    public String getPassiveDescription() {

        return passiveDescription;

    }

    public List<String> getLore() {

        return lore;

    }

}