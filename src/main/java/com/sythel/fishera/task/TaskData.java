package com.sythel.fishera.task;

public class TaskData {

    private final String id;
    private final String name;
    private final String description;
    private final String type;
    private final String fishId;
    private final double amount;
    private final double reward;

    public TaskData(String id,
                    String name,
                    String description,
                    String type,
                    String fishId,
                    double amount,
                    double reward) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.fishId = fishId;
        this.amount = amount;
        this.reward = reward;
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

    public String getType() {
        return type;
    }

    public String getFishId() {
        return fishId;
    }

    public double getAmount() {
        return amount;
    }

    public double getReward() {
        return reward;
    }

}