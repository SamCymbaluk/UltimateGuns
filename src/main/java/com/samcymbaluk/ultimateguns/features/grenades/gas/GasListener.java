package com.samcymbaluk.ultimateguns.features.grenades.gas;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GasListener implements Listener {

    private GasFeature gasFeature;
    private GasManager gasManager;

    public GasListener(GasFeature gasFeature, GasManager gasManager) {
        this.gasFeature = gasFeature;
        this.gasManager = gasManager;
        UltimateGuns.getInstance().getServer().getPluginManager().registerEvents(this, UltimateGuns.getInstance());
    }

    @EventHandler
    public void onPlayerIteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!gasFeature.isEnabled(player.getWorld())) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == gasFeature.getConfig().getItemMaterial()) {
                event.setCancelled(true);
                GasGrenade gas = new GasGrenade(gasManager, gasFeature, player);
                gas.throwGrenade();
            }
        }
    }
}