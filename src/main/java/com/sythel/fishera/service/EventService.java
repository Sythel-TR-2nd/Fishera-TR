package com.sythel.fishera.service;

import com.sythel.fishera.config.ConfigManager;
import com.sythel.fishera.repository.FishRepository;
import com.sythel.fishera.repository.FishRepository.EventCatchCount;
import com.sythel.fishera.repository.FishRepository.EventCatchResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class EventService {

    private final JavaPlugin plugin;

    private final ConfigManager configManager;

    private final FishRepository fishRepository;

    private final EconomyService economyService;

    private BukkitTask eventTask;

    private BukkitTask scheduleTask;

    private boolean active;

    private long startTime;

    private long endTime;

    public EventService(
            JavaPlugin plugin,
            ConfigManager configManager,
            FishRepository fishRepository,
            EconomyService economyService) {

        this.plugin = plugin;

        this.configManager = configManager;

        this.fishRepository = fishRepository;

        this.economyService = economyService;

    }

    public boolean start() {

        if (active) {
            return false;
        }

        int duration =
                configManager.getEventConfig()
                        .getInt(
                                "duration",
                                15
                        );

        if (duration <= 0) {
            return false;
        }

        startTime =
                System.currentTimeMillis();

        endTime =
                startTime
                        + (duration * 60L * 1000L);

        active = true;

        eventTask =
                plugin.getServer()
                        .getScheduler()
                        .runTaskLater(
                                plugin,
                                this::finish,
                                duration * 60L * 20L
                        );

        sendBroadcastList(
                "messages.start",
                "duration",
                String.valueOf(duration)
        );

        return true;

    }

    public boolean stop() {

        if (!active) {
            return false;
        }

        finish();

        return true;

    }

    public void startSchedule() {

        stopSchedule();

        boolean enabled =
                configManager.getEventConfig()
                        .getBoolean(
                                "schedule.enabled",
                                false
                        );

        if (!enabled) {
            return;
        }

        String timeValue =
                configManager.getEventConfig()
                        .getString(
                                "schedule.time",
                                "20:00"
                        );

        LocalTime scheduledTime;

        try {

            scheduledTime =
                    LocalTime.parse(
                            timeValue
                    );

        } catch (Exception exception) {

            plugin.getLogger().warning(
                    "event.yml içindeki schedule.time geçersiz: "
                            + timeValue
            );

            return;

        }

        long initialDelay =
                calculateInitialDelay(
                        scheduledTime
                );

        scheduleTask =
                plugin.getServer()
                        .getScheduler()
                        .runTaskTimer(
                                plugin,
                                () -> {

                                    if (!active) {
                                        start();
                                    }

                                },
                                initialDelay,
                                24L * 60L * 60L * 20L
                        );

        plugin.getLogger().info(
                getConfigMessage(
                        "messages.log.scheduled",
                        "time",
                        timeValue
                )
        );

    }

    public void stopSchedule() {

        if (scheduleTask != null) {

            scheduleTask.cancel();

            scheduleTask = null;

        }

    }

    private long calculateInitialDelay(
            LocalTime scheduledTime) {

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime nextRun =
                LocalDateTime.of(
                        now.toLocalDate(),
                        scheduledTime
                );

        if (!nextRun.isAfter(now)) {

            nextRun =
                    nextRun.plusDays(1);

        }

        long seconds =
                Duration.between(
                        now,
                        nextRun
                ).getSeconds();

        return Math.max(
                1L,
                seconds * 20L
        );

    }

    private void finish() {

        if (!active) {
            return;
        }

        active = false;

        if (eventTask != null) {

            eventTask.cancel();

            eventTask = null;

        }

        long eventEndTime =
                Math.min(
                        System.currentTimeMillis(),
                        endTime
                );

        List<EventCatchCount> catchCounts =
                fishRepository.getCatchCounts(
                        startTime,
                        eventEndTime
                );

        List<EventCatchResult> heaviestCatches =
                fishRepository.getHeaviestCatches(
                        startTime,
                        eventEndTime
                );

        sendBroadcastList(
                "messages.finish"
        );

        distributeMostFishRewards(
                catchCounts
        );

        distributeHeaviestFishRewards(
                heaviestCatches
        );

        plugin.getLogger().info(
                getConfigMessage(
                        "messages.log.finished"
                )
        );

        plugin.getLogger().info(
                getConfigMessage(
                        "messages.log.most-fish",
                        "players",
                        String.valueOf(
                                catchCounts.size()
                        )
                )
        );

        plugin.getLogger().info(
                getConfigMessage(
                        "messages.log.heaviest-fish",
                        "players",
                        String.valueOf(
                                heaviestCatches.size()
                        )
                )
        );

        Bukkit.broadcastMessage("");

    }

    private void distributeMostFishRewards(
            List<EventCatchCount> results) {

        String path =
                "rewards.most-fish";

        for (int i = 0;
             i < results.size() && i < 3;
             i++) {

            EventCatchCount result =
                    results.get(i);

            int place =
                    i + 1;

            double reward =
                    configManager.getEventConfig()
                            .getDouble(
                                    path + "." + place,
                                    0
                            );

            if (reward <= 0) {
                continue;
            }

            giveReward(
                    result.getUuid(),
                    reward
            );

            broadcastWinner(
                    "most-fish",
                    place,
                    result.getPlayerName(),
                    result.getCount(),
                    "balık",
                    reward
            );

        }

    }

    private void distributeHeaviestFishRewards(
            List<EventCatchResult> results) {

        String path =
                "rewards.heaviest-fish";

        for (int i = 0;
             i < results.size() && i < 3;
             i++) {

            EventCatchResult result =
                    results.get(i);

            int place =
                    i + 1;

            double reward =
                    configManager.getEventConfig()
                            .getDouble(
                                    path + "." + place,
                                    0
                            );

            if (reward <= 0) {
                continue;
            }

            giveReward(
                    result.getUuid(),
                    reward
            );

            broadcastWinner(
                    "heaviest-fish",
                    place,
                    result.getPlayerName(),
                    result.getWeight(),
                    "kg",
                    reward
            );

        }

    }

    private void giveReward(
            String uuid,
            double amount) {

        try {

            economyService.deposit(
                    UUID.fromString(uuid),
                    amount
            );

        } catch (IllegalArgumentException exception) {

            plugin.getLogger().warning(
                    getConfigMessage(
                            "messages.log.invalid-uuid",
                            "uuid",
                            uuid
                    )
            );

        }

    }

    private void broadcastWinner(
            String category,
            int place,
            String playerName,
            double value,
            String unit,
            double reward) {

        Bukkit.broadcastMessage(
                getConfigMessage(
                        "messages.winner-broadcast",
                        "place",
                        String.valueOf(place),
                        "player",
                        playerName,
                        "value",
                        formatValue(value),
                        "unit",
                        unit,
                        "reward",
                        formatValue(reward)
                )
        );

        Player player =
                Bukkit.getPlayer(
                        playerName
                );

        if (player != null) {

            player.sendMessage(
                    getConfigMessage(
                            "messages.winner-player.rank",
                            "category",
                            getCategoryName(category),
                            "place",
                            String.valueOf(place)
                    )
            );

            player.sendMessage(
                    getConfigMessage(
                            "messages.winner-player.reward",
                            "reward",
                            formatValue(reward)
                    )
            );

        }

    }

    private void sendBroadcastList(
            String path,
            String... replacements) {

        List<String> messages =
                configManager.getEventConfig()
                        .getStringList(
                                path
                        );

        for (String message : messages) {

            Bukkit.broadcastMessage(
                    formatMessage(
                            message,
                            replacements
                    )
            );

        }

    }

    private String getConfigMessage(
            String path,
            String... replacements) {

        String message =
                configManager.getEventConfig()
                        .getString(
                                path,
                                ""
                        );

        return formatMessage(
                message,
                replacements
        );

    }

    private String formatMessage(
            String message,
            String... replacements) {

        if (message == null) {
            return "";
        }

        for (int i = 0;
             i + 1 < replacements.length;
             i += 2) {

            message =
                    message.replace(
                            "{"
                                    + replacements[i]
                                    + "}",
                            replacements[i + 1]
                    );

        }

        return colorize(
                message
        );

    }

    private String getCategoryName(
            String category) {

        return configManager.getEventConfig()
                .getString(
                        "categories." + category,
                        category
                );

    }

    private String colorize(
            String message) {

        return message.replace(
                "&",
                "§"
        );

    }

    private String formatValue(
            double value) {

        if (value == Math.floor(value)) {

            return String.valueOf(
                    (long) value
            );

        }

        return String.format(
                java.util.Locale.US,
                "%.2f",
                value
        );

    }

    public boolean isActive() {

        return active;

    }

    public long getStartTime() {

        return startTime;

    }

    public long getEndTime() {

        return endTime;

    }

    public List<EventCatchCount> getCatchCounts() {

        if (!active) {
            return List.of();
        }

        return fishRepository.getCatchCounts(
                startTime,
                Math.min(
                        System.currentTimeMillis(),
                        endTime
                )
        );

    }

    public List<EventCatchResult> getHeaviestCatches() {

        if (!active) {
            return List.of();
        }

        return fishRepository.getHeaviestCatches(
                startTime,
                Math.min(
                        System.currentTimeMillis(),
                        endTime
                )
        );

    }

}