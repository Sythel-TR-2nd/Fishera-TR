package com.sythel.fishera.minigame;

public class MinigameRenderer {

    public String render(FishingMinigame minigame) {

        StringBuilder builder = new StringBuilder();

        int cursor = minigame.getCursor();
        int successStart = minigame.getSuccessStart();
        int successEnd = minigame.getSuccessEnd();

        boolean cursorInSuccess =
                cursor >= successStart
                        && cursor <= successEnd;


        builder.append("§8‹ ");

        for (int i = 0; i < 17; i++) {

            boolean cursorHere =
                    i == cursor;

            boolean successHere =
                    i >= successStart
                            && i <= successEnd;


            if (cursorHere && successHere) {

                builder.append("§a§l🪝");

            } else if (cursorHere) {

                builder.append("§f§l🪝");

            } else if (successHere) {

                builder.append("§e🐟");

            } else {

                builder.append("§b≈");
            }
        }

        builder.append(" §8› ");

        if (cursorInSuccess) {

            builder.append("§a§lTIKLA!");

        } else {

            builder.append("§7Hedefle");
        }

        return builder.toString();
    }
}