package com.sythel.fishera.util;

import java.util.concurrent.ThreadLocalRandom;

public final class WeightUtil {

    private WeightUtil() {
    }

    public static double generate(double minWeight,
                                  double maxWeight,
                                  int rodWeight) {

        double random =
                ThreadLocalRandom.current()
                        .nextDouble();

        double bonus =
                Math.min(
                        rodWeight / 2000.0,
                        0.35
                );

        random =
                Math.min(
                        random + bonus,
                        1.0
                );

        double weight =
                minWeight
                        + (maxWeight - minWeight)
                        * random;

        return Math.round(weight * 100.0) / 100.0;

    }

}