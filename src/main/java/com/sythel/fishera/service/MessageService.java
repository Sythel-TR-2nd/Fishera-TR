package com.sythel.fishera.service;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.entity.Player;

public class MessageService {

    private static final String PREFIX = "messages.";

    private final ConfigManager configManager;

    public MessageService(
            ConfigManager configManager) {

        this.configManager = configManager;

    }

    public String get(
            String path) {

        String message =
                configManager.getMessagesConfig()
                        .getString(
                                PREFIX + path
                        );

        if (message == null) {

            return "§cMesaj bulunamadı: " + path;

        }

        return ColorUtil.color(
                message
        );

    }

    public String get(
            String path,
            String... replacements) {

        String message =
                get(path);

        for (int i = 0;
             i + 1 < replacements.length;
             i += 2) {

            String placeholder =
                    replacements[i];

            String value =
                    replacements[i + 1];

            value =
                    ColorUtil.color(
                            value
                    );

            message =
                    message.replace(
                            "{" + placeholder + "}",
                            value
                    );

        }

        return message;

    }

    public void send(
            Player player,
            String path) {

        player.sendMessage(
                get(path)
        );

    }

    public void send(
            Player player,
            String path,
            String... replacements) {

        player.sendMessage(
                get(
                        path,
                        replacements
                )
        );

    }

}