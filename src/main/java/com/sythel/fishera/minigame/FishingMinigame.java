package com.sythel.fishera.minigame;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public class FishingMinigame {

    private final Player player;

    private int cursor;

    private boolean movingRight;

    private int successStart;
    private int successEnd;

    private BukkitTask task;

    private boolean canClick;

    public FishingMinigame(Player player) {

        this.player = player;

        this.cursor = 0;
        this.movingRight = true;

        this.canClick = false;

        generateSuccessZone();

    }

    private void generateSuccessZone() {

        successStart = ThreadLocalRandom.current().nextInt(3, 13);

        successEnd = successStart + 2;

    }

    public Player getPlayer() {

        return player;

    }

    public int getCursor() {

        return cursor;

    }

    public void setCursor(int cursor) {

        this.cursor = cursor;

    }

    public boolean isMovingRight() {

        return movingRight;

    }

    public void setMovingRight(boolean movingRight) {

        this.movingRight = movingRight;

    }

    public int getSuccessStart() {

        return successStart;

    }

    public int getSuccessEnd() {

        return successEnd;

    }

    public boolean isSuccess() {

        return cursor >= successStart && cursor <= successEnd;

    }

    public BukkitTask getTask() {

        return task;

    }

    public void setTask(BukkitTask task) {

        this.task = task;

    }

    public boolean canClick() {

        return canClick;

    }

    public void setCanClick(boolean canClick) {

        this.canClick = canClick;

    }

}