package com.sythel.fishera.registry;

import com.sythel.fishera.bait.BaitData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaitRegistry {

    private final Map<String, BaitData> baits =
            new LinkedHashMap<>();

    public void register(BaitData bait) {

        baits.put(
                bait.getId(),
                bait
        );

    }

    public BaitData get(String id) {

        if (id == null) {
            return null;
        }

        return baits.get(id);

    }

    public Collection<BaitData> getAll() {

        return baits.values();

    }

    public void clear() {

        baits.clear();

    }

}