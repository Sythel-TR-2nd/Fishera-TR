package com.sythel.fishera.simulation;

import com.sythel.fishera.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationConfig {

    private final ConfigManager configManager;

    public SimulationConfig(ConfigManager configManager) {
        this.configManager = configManager;
    }

    private FileConfiguration getConfig() {
        return configManager.getSimulationConfig();
    }

    public String getTitle() {
        return getConfig().getString(
                "simulation.title",
                "Fishera • Ekonomi Simülasyonu"
        );
    }

    public Material getBackgroundMaterial() {
        return getMaterial(
                "simulation.background.material",
                Material.BLACK_STAINED_GLASS_PANE
        );
    }

    public String getBackgroundName() {
        return getConfig().getString(
                "simulation.background.name",
                " "
        );
    }

    public Material getButtonMaterial(String button) {
        return getMaterial(
                "simulation.buttons." + button + ".material",
                Material.PAPER
        );
    }

    public String getButtonName(String button) {
        return getConfig().getString(
                "simulation.buttons." + button + ".name",
                button
        );
    }

    public List<String> getButtonLore(String button) {
        return getConfig().getStringList(
                "simulation.buttons." + button + ".lore"
        );
    }

    public Material getResultMaterial() {
        return getMaterial(
                "simulation.result.material",
                Material.BOOK
        );
    }

    public String getResultName() {
        return getConfig().getString(
                "simulation.result.name",
                "Simülasyon Sonucu"
        );
    }

    public List<String> getResultLore() {
        return getConfig().getStringList(
                "simulation.result.lore"
        );
    }

    public String getComparisonTitle() {
        return getConfig().getString(
                "simulation.comparison.title",
                "Fishera • Olta Karşılaştırması"
        );
    }

    public Material getComparisonEntryMaterial() {
        return getMaterial(
                "simulation.comparison.entry.material",
                Material.FISHING_ROD
        );
    }

    public String getComparisonEntryName() {
        return getConfig().getString(
                "simulation.comparison.entry.name",
                "#%place% %rod%"
        );
    }

    public List<String> getComparisonEntryLore() {
        return getConfig().getStringList(
                "simulation.comparison.entry.lore"
        );
    }

    public Material getComparisonEmptyMaterial() {
        return getMaterial(
                "simulation.comparison.empty.material",
                Material.BARRIER
        );
    }

    public String getComparisonEmptyName() {
        return getConfig().getString(
                "simulation.comparison.empty.name",
                "Karşılaştırılacak olta yok."
        );
    }

    public List<String> getComparisonEmptyLore() {
        return getConfig().getStringList(
                "simulation.comparison.empty.lore"
        );
    }

    public String getDurationTitle() {
        return getConfig().getString(
                "simulation.duration.title",
                "Fishera • Simülasyon Süresi"
        );
    }

    public Material getDurationBackgroundMaterial() {
        return getMaterial(
                "simulation.duration.background.material",
                Material.BLACK_STAINED_GLASS_PANE
        );
    }

    public String getDurationBackgroundName() {
        return getConfig().getString(
                "simulation.duration.background.name",
                " "
        );
    }

    public Material getDurationEntryMaterial() {
        return getMaterial(
                "simulation.duration.entry.material",
                Material.CLOCK
        );
    }

    public String getDurationEntryName() {
        return getConfig().getString(
                "simulation.duration.entry.name",
                "%duration%"
        );
    }

    public List<String> getDurationEntryLore() {
        return getConfig().getStringList(
                "simulation.duration.entry.lore"
        );
    }

    public Material getDurationBackMaterial() {
        return getMaterial(
                "simulation.duration.back.material",
                Material.ARROW
        );
    }

    public String getDurationBackName() {
        return getConfig().getString(
                "simulation.duration.back.name",
                "◀ Geri"
        );
    }

    public List<String> getDurationBackLore() {
        return getConfig().getStringList(
                "simulation.duration.back.lore"
        );
    }

    public String getSelectionTitle(String type) {
        return getConfig().getString(
                "simulation.selection.title",
                "Fishera • %type% Seçimi"
        ).replace("%type%", type);
    }

    public Material getSelectionBackgroundMaterial() {
        return getMaterial(
                "simulation.selection.background.material",
                Material.BLACK_STAINED_GLASS_PANE
        );
    }

    public String getSelectionBackgroundName() {
        return getConfig().getString(
                "simulation.selection.background.name",
                " "
        );
    }

    public String getSelectionSuffix() {
        return getConfig().getString(
                "simulation.selection.selected.suffix",
                " &a✔"
        );
    }

    public Material getSelectionBackMaterial() {
        return getMaterial(
                "simulation.selection.back.material",
                Material.ARROW
        );
    }

    public String getSelectionBackName() {
        return getConfig().getString(
                "simulation.selection.back.name",
                "◀ Geri"
        );
    }

    public List<String> getSelectionBackLore() {
        return getConfig().getStringList(
                "simulation.selection.back.lore"
        );
    }

    public List<String> getMessage(String key) {
        return getConfig().getStringList(
                "simulation.messages." + key
        );
    }

    public int getDefaultDuration() {
        return getConfig().getInt(
                "simulation.settings.default-duration",
                3600
        );
    }

    public String getSuccessAssumption() {
        return getConfig().getString(
                "simulation.settings.success-assumption",
                "%100 başarılı minigame varsayımı"
        );
    }

    public List<Integer> getDurationOptions() {
        List<Integer> durations =
                getConfig().getIntegerList(
                        "simulation.settings.duration-options"
                );

        if (durations.isEmpty()) {
            return Collections.singletonList(3600);
        }

        return new ArrayList<>(durations);
    }

    public String getDurationName(int duration) {
        return getConfig().getString(
                "simulation.duration." + duration + ".name",
                formatDuration(duration)
        );
    }

    private Material getMaterial(
            String path,
            Material fallback) {

        String value =
                getConfig().getString(path);

        if (value == null) {
            return fallback;
        }

        Material material =
                Material.matchMaterial(value);

        return material != null
                ? material
                : fallback;
    }

    private String formatDuration(int seconds) {
        if (seconds < 60) {
            return seconds + " Saniye";
        }

        if (seconds % 3600 == 0) {
            return (seconds / 3600) + " Saat";
        }

        return (seconds / 60) + " Dakika";
    }
}