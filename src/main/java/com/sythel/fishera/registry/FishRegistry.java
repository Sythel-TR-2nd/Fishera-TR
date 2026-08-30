package com.sythel.fishera.registry;

import com.sythel.fishera.fish.FishData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class FishRegistry {

    private final Map<String, FishData> fishes = new LinkedHashMap<>();

    public void register(FishData fish) {

        fishes.put(
                fish.getId().toLowerCase(),
                fish
        );

    }

    public FishData get(String id) {

        return fishes.get(id.toLowerCase());

    }

    public boolean contains(String id) {

        return fishes.containsKey(id.toLowerCase());

    }

    public Collection<FishData> getAll() {

        return fishes.values();

    }

    public void clear() {

        fishes.clear();

    }

}