package com.sythel.fishera.registry;

import com.sythel.fishera.rarity.RarityData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RarityRegistry {

    private final Map<String, RarityData> rarities = new LinkedHashMap<>();

    public void register(RarityData rarity) {

        rarities.put(
                rarity.getId().toLowerCase(),
                rarity
        );

    }

    public RarityData get(String id) {

        return rarities.get(id.toLowerCase());

    }

    public boolean contains(String id) {

        return rarities.containsKey(id.toLowerCase());

    }

    public Collection<RarityData> getAll() {

        return rarities.values();

    }

    public void clear() {

        rarities.clear();

    }

}