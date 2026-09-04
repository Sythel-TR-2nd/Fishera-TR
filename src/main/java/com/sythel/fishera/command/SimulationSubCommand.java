package com.sythel.fishera.command;

import com.sythel.fishera.simulation.SimulationMenu;
import org.bukkit.entity.Player;

public class SimulationSubCommand implements SubCommand {

    private final SimulationMenu simulationMenu;

    public SimulationSubCommand(
            SimulationMenu simulationMenu) {

        this.simulationMenu = simulationMenu;
    }

    @Override
    public String getName() {
        return "simulation";
    }

    @Override
    public void execute(
            Player player,
            String[] args) {

        simulationMenu.open(player);
    }
}