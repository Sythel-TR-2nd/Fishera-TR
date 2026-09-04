package com.sythel.fishera.bootstrap;

import com.sythel.fishera.Fishera;
import com.sythel.fishera.simulation.EconomySimulationService;
import com.sythel.fishera.simulation.SimulationComparisonMenu;
import com.sythel.fishera.simulation.SimulationConfig;
import com.sythel.fishera.simulation.SimulationDurationMenu;
import com.sythel.fishera.simulation.SimulationDurationMenuListener;
import com.sythel.fishera.simulation.SimulationMenu;
import com.sythel.fishera.simulation.SimulationMenuListener;
import com.sythel.fishera.simulation.SimulationResultMenu;
import com.sythel.fishera.simulation.SimulationSelectionMenu;
import com.sythel.fishera.simulation.SimulationSelectionMenuListener;
import com.sythel.fishera.simulation.SimulationSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class SimulationSetup {

    private final Fishera plugin;
    private final EconomySimulationService simulationService;
    private final SimulationConfig simulationConfig;

    private SimulationSessionManager sessionManager;
    private SimulationMenu simulationMenu;
    private SimulationSelectionMenu selectionMenu;
    private SimulationDurationMenu durationMenu;
    private SimulationResultMenu resultMenu;
    private SimulationComparisonMenu comparisonMenu;

    public SimulationSetup(
            Fishera plugin,
            EconomySimulationService simulationService,
            SimulationConfig simulationConfig) {

        this.plugin = plugin;
        this.simulationService = simulationService;
        this.simulationConfig = simulationConfig;
    }

    public void initialize() {

        sessionManager =
                new SimulationSessionManager();

        simulationMenu =
                new SimulationMenu(
                        simulationService,
                        simulationConfig,
                        sessionManager
                );

        selectionMenu =
                new SimulationSelectionMenu(
                        simulationService,
                        simulationConfig
                );

        durationMenu =
                new SimulationDurationMenu(
                        simulationConfig
                );

        resultMenu =
                new SimulationResultMenu(
                        simulationConfig
                );

        comparisonMenu =
                new SimulationComparisonMenu(
                        simulationService,
                        simulationConfig
                );

        PluginManager pluginManager =
                Bukkit.getPluginManager();

        pluginManager.registerEvents(
                new SimulationMenuListener(
                        simulationMenu,
                        selectionMenu,
                        durationMenu,
                        resultMenu,
                        comparisonMenu,
                        sessionManager,
                        simulationConfig
                ),
                plugin
        );

        pluginManager.registerEvents(
                new SimulationSelectionMenuListener(
                        selectionMenu,
                        sessionManager,
                        simulationMenu,
                        simulationConfig
                ),
                plugin
        );

        pluginManager.registerEvents(
                new SimulationDurationMenuListener(
                        durationMenu,
                        sessionManager,
                        simulationMenu,
                        simulationConfig
                ),
                plugin
        );
    }

    public SimulationMenu getSimulationMenu() {
        return simulationMenu;
    }

    public SimulationSessionManager getSessionManager() {
        return sessionManager;
    }
}