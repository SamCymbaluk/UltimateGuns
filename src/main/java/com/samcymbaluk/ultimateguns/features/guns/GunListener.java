package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class GunListener implements Listener {

    public GunListener() {
        UltimateGuns.getInstance().getServer().getPluginManager().registerEvents(this, UltimateGuns.getInstance());
    }

    @EventHandler
    public void onPlayerIteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (GunFeature.getInstance().isGun(item.getType())) {
            Gun gun = Gun.loadGun(item);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                gun.fire(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();

        if (item != null && GunFeature.getInstance().isGun(item.getType())) {
            event.setCancelled(true);
            // Loading 'item' would result in item desync in Gun
            Gun gun = Gun.loadGun(event.getPlayer().getInventory().getItemInMainHand());
            gun.reload(UltimateGuns.getInstance().getGunPlayer(event.getPlayer()));
        }
    }
}
