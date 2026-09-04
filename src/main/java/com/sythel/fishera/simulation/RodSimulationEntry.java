package com.sythel.fishera.simulation;

import com.sythel.fishera.rod.RodData;

public class RodSimulationEntry {

    private final RodData rod;
    private final SimulationResult result;

    public RodSimulationEntry(
            RodData rod,
            SimulationResult result
    ) {
        this.rod = rod;
        this.result = result;
    }

    public RodData getRod() {
        return rod;
    }

    public SimulationResult getResult() {
        return result;
    }
}