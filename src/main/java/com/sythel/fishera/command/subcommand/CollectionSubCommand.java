package com.sythel.fishera.command.subcommand;

import com.sythel.fishera.collection.CollectionMenu;
import com.sythel.fishera.command.SubCommand;
import org.bukkit.entity.Player;

public class CollectionSubCommand implements SubCommand {

    private final CollectionMenu collectionMenu;

    public CollectionSubCommand(CollectionMenu collectionMenu) {

        this.collectionMenu = collectionMenu;

    }

    @Override
    public String getName() {

        return "collection";

    }

    @Override
    public void execute(Player player, String[] args) {

        collectionMenu.open(player);

    }

}