package com.sythel.fishera.util;

import com.sythel.fishera.fish.CaughtFish;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PlaceholderUtil {

    private PlaceholderUtil() {
    }

    public static List<String> apply(List<String> lore,
                                     CaughtFish fish) {

        List<String> result = new ArrayList<>();

        for (String line : lore) {

            line = line.replace(
                    "%fish%",
                    fish.getFishData().getName()
            );

            line = line.replace(
                    "%weight%",
                    format(fish.getWeight())
            );

            line = line.replace(
                    "%price%",
                    format(fish.getPrice())
            );

            line = line.replace(
                    "%rarity%",
                    fish.getRarityData().getName()
            );

            result.add(line);

        }

        return result;

    }

    private static String format(double value) {

        DecimalFormatSymbols symbols =
                DecimalFormatSymbols.getInstance(
                        Locale.forLanguageTag("tr-TR")
                );

        DecimalFormat decimal =
                new DecimalFormat(
                        "#,##0.00",
                        symbols
                );

        return decimal.format(value);

    }

}