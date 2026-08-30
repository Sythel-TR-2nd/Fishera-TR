package com.sythel.fishera.bait;

public class BaitData {

    private final String id;

    private final String name;

    private final String description;

    private final String material;

    private final int customModelData;

    private final double price;

    private final int weight;

    private final int speed;

    private final int luck;

    private final int maxAmount;

    public BaitData(String id,
                    String name,
                    String description,
                    String material,
                    int customModelData,
                    double price,
                    int weight,
                    int speed,
                    int luck,
                    int maxAmount) {

        this.id = id;

        this.name = name;

        this.description = description;

        this.material = material;

        this.customModelData = customModelData;

        this.price = price;

        this.weight = weight;

        this.speed = speed;

        this.luck = luck;

        this.maxAmount = maxAmount;

    }

    public String getId() {

        return id;

    }

    public String getName() {

        return name;

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

    public int getWeight() {

        return weight;

    }

    public int getSpeed() {

        return speed;

    }

    public int getLuck() {

        return luck;

    }

    public int getMaxAmount() {

        return maxAmount;

    }

}