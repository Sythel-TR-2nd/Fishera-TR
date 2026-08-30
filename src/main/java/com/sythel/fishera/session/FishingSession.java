package com.sythel.fishera.session;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class FishingSession {

    public enum State {

        WAITING,
        BITING,
        MINIGAME

    }

    private final Player player;

    private State state;

    private BukkitTask waitingTask;
    private BukkitTask escapeTask;

    public FishingSession(Player player) {

        this.player = player;
        this.state = State.WAITING;

    }

    public Player getPlayer() {

        return player;

    }

    public State getState() {

        return state;

    }

    public void setState(State state) {

        this.state = state;

    }

    public BukkitTask getWaitingTask() {

        return waitingTask;

    }

    public void setWaitingTask(BukkitTask waitingTask) {

        this.waitingTask = waitingTask;

    }

    public BukkitTask getEscapeTask() {

        return escapeTask;

    }

    public void setEscapeTask(BukkitTask escapeTask) {

        this.escapeTask = escapeTask;

    }

    public void destroy() {

        if (waitingTask != null) {
            waitingTask.cancel();
            waitingTask = null;
        }

        if (escapeTask != null) {
            escapeTask.cancel();
            escapeTask = null;
        }

    }

}