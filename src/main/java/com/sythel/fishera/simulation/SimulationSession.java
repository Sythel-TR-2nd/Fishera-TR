package com.sythel.fishera.simulation;

public class SimulationSession {

    private String rodId;
    private String baitId;
    private int durationSeconds;

    public SimulationSession(int defaultDuration) {
        this.durationSeconds = defaultDuration;
    }

    public String getRodId() {
        return rodId;
    }

    public void setRodId(String rodId) {
        this.rodId = rodId;
    }

    public String getBaitId() {
        return baitId;
    }

    public void setBaitId(String baitId) {
        this.baitId = baitId;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}