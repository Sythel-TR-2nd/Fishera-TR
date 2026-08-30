package com.sythel.fishera.top;

public class TopEntry {

    private final String playerName;

    private final double value;

    private final String extra;

    public TopEntry(String playerName,
                    double value,
                    String extra) {

        this.playerName = playerName;
        this.value = value;
        this.extra = extra;

    }

    public String getPlayerName() {

        return playerName;

    }

    public double getValue() {

        return value;

    }

    public String getExtra() {

        return extra;

    }

}