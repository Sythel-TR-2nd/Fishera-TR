package com.sythel.fishera.command;

import org.bukkit.entity.Player;

public interface SubCommand {

    String getName();

    void execute(Player player, String[] args);

}