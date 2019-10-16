package com.samcymbaluk.ultimateguns.features.shield;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ShieldListener implements Listener {

    public ShieldListener() {
        UltimateGuns.getInstance().getServer().getPluginManager().registerEvents(this, UltimateGuns.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ShieldFeature.getInstance().addShieldTarget(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ShieldFeature.getInstance().removeShieldTarget(event.getPlayer());
    }
}
