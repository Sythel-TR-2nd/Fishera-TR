package com.sythel.fishera.util;

import java.util.Locale;

public final class RarityUtil {

    private RarityUtil() {
    }

    public static String getDisplayName(String rarity) {

        String value =
                rarity.toLowerCase(Locale.ROOT);

        return switch (value) {

            case "common" ->
                    ColorUtil.color("&#8BC34A★ Common");

            case "uncommon" ->
                    ColorUtil.color("&#29B6F6★★ Uncommon");

            case "rare" ->
                    ColorUtil.color("&#AB47BC★★★ Rare");

            case "epic" ->
                    ColorUtil.color("&#FB8C00★★★★ Epic");

            case "legendary" ->
                    ColorUtil.color("&#E53935★★★★★ Legendary");

            default ->
                    ColorUtil.color("&7Unknown");

        };

    }

    public static String getDescription(String rarity) {

        String value =
                rarity.toLowerCase(Locale.ROOT);

        return switch (value) {

            case "common" ->
                    "Her balıkçının tanıdığı sıradan bir tür.";

            case "uncommon" ->
                    "Deneyimli balıkçıların daha sık karşılaştığı bir tür.";

            case "rare" ->
                    "Yakalanması sabır ve şans gerektirir.";

            case "epic" ->
                    "Adı balıkçılar arasında saygıyla anılır.";

            case "legendary" ->
                    "Onu yakalayanlar hikâyesini yıllarca anlatır.";

            default ->
                    "Bilinmeyen nadirlik.";

        };

    }

    public static int getStars(String rarity) {

        String value =
                rarity.toLowerCase(Locale.ROOT);

        return switch (value) {

            case "common" -> 1;

            case "uncommon" -> 2;

            case "rare" -> 3;

            case "epic" -> 4;

            case "legendary" -> 5;

            default -> 0;

        };

    }

    public static String getPlainName(String rarity) {

        String value =
                rarity.toLowerCase(Locale.ROOT);

        return switch (value) {

            case "common" ->
                    "Common";

            case "uncommon" ->
                    "Uncommon";

            case "rare" ->
                    "Rare";

            case "epic" ->
                    "Epic";

            case "legendary" ->
                    "Legendary";

            default ->
                    "Unknown";

        };

    }

}