package com.sythel.fishera.rarity;

import java.util.List;

public class RarityData {

    private final String id;

    private final String name;

    private final String color;

    private final List<String> description;

    public RarityData(String id,
                      String name,
                      String color,
                      List<String> description) {

        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<String> getDescription() {
        return description;
    }

}