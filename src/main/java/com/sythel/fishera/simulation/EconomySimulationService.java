package com.sythel.fishera.simulation;

import com.sythel.fishera.bait.BaitData;
import com.sythel.fishera.fish.FishData;
import com.sythel.fishera.registry.BaitRegistry;
import com.sythel.fishera.registry.FishRegistry;
import com.sythel.fishera.registry.RodRegistry;
import com.sythel.fishera.rod.RodData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EconomySimulationService {

    private final SimulationEngine engine;
    private final RodRegistry rodRegistry;
    private final BaitRegistry baitRegistry;

    public EconomySimulationService(
            RodRegistry rodRegistry,
            BaitRegistry baitRegistry,
            FishRegistry fishRegistry
    ) {
        this.rodRegistry = rodRegistry;
        this.baitRegistry = baitRegistry;
        this.engine = new SimulationEngine(
                fishRegistry
        );
    }

    public SimulationResult calculate(
            String rodId,
            String baitId,
            int durationSeconds
    ) {
        RodData rod =
                rodRegistry.get(rodId);

        if (rod == null) {
            return new SimulationResult(
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );
        }

        BaitData bait =
                baitId == null
                        ? null
                        : baitRegistry.get(baitId);

        return engine.calculate(
                rod,
                bait,
                durationSeconds
        );
    }

    public List<RodSimulationEntry> compareRods(
            BaitData bait
    ) {
        List<RodSimulationEntry> results =
                new ArrayList<>();

        for (RodData rod :
                rodRegistry.getAll()) {

            SimulationResult result =
                    engine.calculate(
                            rod,
                            bait,
                            3600
                    );

            results.add(
                    new RodSimulationEntry(
                            rod,
                            result
                    )
            );
        }

        results.sort(
                Comparator.comparingDouble(
                        entry ->
                                -entry.getResult()
                                        .getNetIncome()
                )
        );

        return results;
    }

    public RodData getRod(
            String rodId
    ) {
        return rodRegistry.get(rodId);
    }

    public BaitData getBait(
            String baitId
    ) {
        return baitRegistry.get(baitId);
    }

    public RodRegistry getRodRegistry() {
        return rodRegistry;
    }

    public BaitRegistry getBaitRegistry() {
        return baitRegistry;
    }
}