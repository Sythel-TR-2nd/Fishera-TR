package com.sythel.fishera.update;

import com.sythel.fishera.Fishera;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateChecker {

    private final Fishera plugin;

    private String latestVersion;

    public UpdateChecker(
            Fishera plugin) {

        this.plugin = plugin;
    }

    public void check() {

        plugin.getServer()
                .getScheduler()
                .runTaskAsynchronously(
                        plugin,
                        () -> {

                            try {

                                HttpClient client =
                                        HttpClient.newHttpClient();

                                HttpRequest request =
                                        HttpRequest.newBuilder()
                                                .uri(
                                                        URI.create(
                                                                "https://api.github.com/repos/Sythel-TR-2nd/Fishera-TR/releases/latest"
                                                        )
                                                )
                                                .header(
                                                        "Accept",
                                                        "application/vnd.github+json"
                                                )
                                                .header(
                                                        "User-Agent",
                                                        "Fishera"
                                                )
                                                .GET()
                                                .build();

                                HttpResponse<String> response =
                                        client.send(
                                                request,
                                                HttpResponse.BodyHandlers.ofString()
                                        );

                                if (response.statusCode() != 200) {
                                    return;
                                }

                                String key =
                                        "\"tag_name\":\"";

                                int start =
                                        response.body().indexOf(
                                                key
                                        );

                                if (start == -1) {
                                    return;
                                }

                                start += key.length();

                                int end =
                                        response.body().indexOf(
                                                "\"",
                                                start
                                        );

                                if (end == -1) {
                                    return;
                                }

                                String tag =
                                        response.body().substring(
                                                start,
                                                end
                                        );

                                latestVersion =
                                        tag.startsWith("v")
                                                ? tag.substring(1)
                                                : tag;

                                String currentVersion =
                                        plugin.getDescription()
                                                .getVersion();

                                if (!currentVersion.equals(
                                        latestVersion
                                )) {

                                    plugin.getLogger().warning(
                                            "Yeni bir Fishera sürümü mevcut!"
                                    );

                                    plugin.getLogger().warning(
                                            "Mevcut sürüm: "
                                                    + currentVersion
                                    );

                                    plugin.getLogger().warning(
                                            "Yeni sürüm: "
                                                    + latestVersion
                                    );

                                    plugin.getLogger().warning(
                                            "https://github.com/Sythel-TR-2nd/Fishera-TR/releases/latest"
                                    );
                                }

                            } catch (
                                    IOException
                                            | InterruptedException
                                            | RuntimeException ignored) {

                            }

                        }
                );
    }

    public String getLatestVersion() {

        return latestVersion;

    }

    public boolean isUpdateAvailable() {

        if (latestVersion == null) {
            return false;
        }

        return !plugin.getDescription()
                .getVersion()
                .equals(
                        latestVersion
                );
    }
}