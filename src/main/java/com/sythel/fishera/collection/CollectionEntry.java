package com.sythel.fishera.collection;

import com.sythel.fishera.fish.FishData;

public class CollectionEntry {

    private final FishData fishData;

    private final boolean caught;

    private final int catchCount;

    private final double bestWeight;

    private final double bestPrice;

    private final long firstCatch;

    public CollectionEntry(FishData fishData,
                           boolean caught,
                           int catchCount,
                           double bestWeight,
                           double bestPrice,
                           long firstCatch) {

        this.fishData = fishData;
        this.caught = caught;
        this.catchCount = catchCount;
        this.bestWeight = bestWeight;
        this.bestPrice = bestPrice;
        this.firstCatch = firstCatch;

    }

    public FishData getFishData() {

        return fishData;

    }

    public boolean isCaught() {

        return caught;

    }

    public int getCatchCount() {

        return catchCount;

    }

    public double getBestWeight() {

        return bestWeight;

    }

    public double getBestPrice() {

        return bestPrice;

    }

    public long getFirstCatch() {

        return firstCatch;

    }

}