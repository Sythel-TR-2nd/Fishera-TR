package com.sythel.fishera.util;

public final class PriceUtil {

    private PriceUtil() {
    }

    public static double calculate(double basePrice,
                                   double weight,
                                   int sellBonus) {

        double price =
                basePrice * weight;

        price *=
                1.0 + (sellBonus / 100.0);

        return Math.round(
                price * 100.0
        ) / 100.0;

    }

}