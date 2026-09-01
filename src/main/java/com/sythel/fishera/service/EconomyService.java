package com.sythel.fishera.service;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class EconomyService {

    private final Economy economy;

    public EconomyService(JavaPlugin plugin) {

        RegisteredServiceProvider<Economy> provider =
                plugin.getServer()
                        .getServicesManager()
                        .getRegistration(Economy.class);

        if (provider == null) {
            throw new IllegalStateException(
                    "Vault Economy bulunamadı."
            );
        }

        this.economy =
                provider.getProvider();

    }

    public void deposit(
            Player player,
            double amount) {

        economy.depositPlayer(
                player,
                amount
        );

    }

    public void deposit(
            UUID uuid,
            double amount) {

        OfflinePlayer player =
                Bukkit.getOfflinePlayer(
                        uuid
                );

        economy.depositPlayer(
                player,
                amount
        );

    }

    public boolean withdraw(
            Player player,
            double amount) {

        return economy.withdrawPlayer(
                player,
                amount
        ).transactionSuccess();

    }

    public double getBalance(
            Player player) {

        return economy.getBalance(
                player
        );

    }

}
