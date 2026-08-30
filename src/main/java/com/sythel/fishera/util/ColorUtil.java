package com.sythel.fishera.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ColorUtil {

    private static final Pattern HEX_PATTERN =
            Pattern.compile("&#([A-Fa-f0-9]{6})");

    private ColorUtil() {
    }

    public static String color(String text) {

        if (text == null) {
            return "";
        }

        Matcher matcher = HEX_PATTERN.matcher(text);

        while (matcher.find()) {

            String hex = matcher.group(1);

            text = text.replace(
                    "&#" + hex,
                    ChatColor.of("#" + hex).toString()
            );

            matcher = HEX_PATTERN.matcher(text);

        }

        return ChatColor.translateAlternateColorCodes('&', text);

    }

    public static List<String> color(List<String> lines) {

        return lines.stream()
                .map(ColorUtil::color)
                .collect(Collectors.toList());

    }

}