package com.sythel.fishera.registry;

import com.sythel.fishera.rod.RodData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RodRegistry {

    private final Map<String, RodData> rods =
            new LinkedHashMap<>();

    public void register(RodData rod) {

        rods.put(
                rod.getId(),
                rod
        );

    }

    public RodData get(String id) {

        return rods.get(id);

    }

    public Collection<RodData> getAll() {

        return rods.values();

    }

    public boolean contains(String id) {

        return rods.containsKey(id);

    }

    public int size() {

        return rods.size();

    }

    public void clear() {

        rods.clear();

    }

}