package com.sythel.fishera.simulation;

public class SimulationResult {

    private final double catchesPerHour;
    private final double averageFishValue;
    private final double grossIncome;
    private final double baitCost;
    private final double netIncome;

    public SimulationResult(
            double catchesPerHour,
            double averageFishValue,
            double grossIncome,
            double baitCost,
            double netIncome
    ) {
        this.catchesPerHour = catchesPerHour;
        this.averageFishValue = averageFishValue;
        this.grossIncome = grossIncome;
        this.baitCost = baitCost;
        this.netIncome = netIncome;
    }

    public double getCatchesPerHour() {
        return catchesPerHour;
    }

    public double getAverageFishValue() {
        return averageFishValue;
    }

    public double getGrossIncome() {
        return grossIncome;
    }

    public double getBaitCost() {
        return baitCost;
    }

    public double getNetIncome() {
        return netIncome;
    }
}