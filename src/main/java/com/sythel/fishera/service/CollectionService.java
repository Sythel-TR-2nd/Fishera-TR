package com.sythel.fishera.service;

import com.sythel.fishera.collection.CollectionEntry;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.repository.CollectionRepository;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CollectionService {

    private final FishRegistry fishRegistry;
    private final CollectionRepository repository;

    public CollectionService(FishRegistry fishRegistry,
                             CollectionRepository repository) {

        this.fishRegistry = fishRegistry;
        this.repository = repository;

    }

    public List<CollectionEntry> getCollection(Player player) {

        List<CollectionEntry> entries = new ArrayList<>();

        for (FishData fish : fishRegistry.getAll()) {

            entries.add(new CollectionEntry(

                    fish,

                    repository.hasCaught(
                            player.getUniqueId(),
                            fish.getId()
                    ),

                    repository.getCatchCount(
                            player.getUniqueId(),
                            fish.getId()
                    ),

                    repository.getBestWeight(
                            player.getUniqueId(),
                            fish.getId()
                    ),

                    repository.getBestPrice(
                            player.getUniqueId(),
                            fish.getId()
                    ),

                    repository.getFirstCatch(
                            player.getUniqueId(),
                            fish.getId()
                    )

            ));

        }

        return entries;

    }

    public int getTotalFish() {

        return fishRegistry.getAll().size();

    }

    public int getCaughtFish(Player player) {

        int count = 0;

        for (CollectionEntry entry : getCollection(player)) {

            if (entry.isCaught()) {
                count++;
            }

        }

        return count;

    }

    public int getMissingFish(Player player) {

        return getTotalFish() - getCaughtFish(player);

    }

    public double getCompletion(Player player) {

        if (getTotalFish() == 0) {
            return 0D;
        }

        return (getCaughtFish(player) * 100D) / getTotalFish();

    }

    public int getTotalByRarity(String rarity) {

        int count = 0;

        for (FishData fish : fishRegistry.getAll()) {

            if (fish.getRarity().equalsIgnoreCase(rarity)) {
                count++;
            }

        }

        return count;

    }

    public int getCaughtByRarity(Player player,
                                 String rarity) {

        int count = 0;

        for (CollectionEntry entry : getCollection(player)) {

            if (!entry.isCaught()) {
                continue;
            }

            if (entry.getFishData()
                    .getRarity()
                    .equalsIgnoreCase(rarity)) {

                count++;

            }

        }

        return count;

    }

    public double getCompletionByRarity(Player player,
                                        String rarity) {

        int total = getTotalByRarity(rarity);

        if (total == 0) {
            return 0D;
        }

        return (getCaughtByRarity(player, rarity) * 100D) / total;

    }

}