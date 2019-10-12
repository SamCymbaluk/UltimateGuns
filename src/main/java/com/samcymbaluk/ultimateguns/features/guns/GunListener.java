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

        if (GunFeature.getInstance().getConfig().isGunMaterial(item.getType())) {
            Gun gun = Gun.loadGun(item);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                gun.handleClick(event);
                event.setCancelled(true);
            } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) { //TODO debug
                GunAmmo ammoType = GunFeature.getInstance().getConfig().getGunAmmo(gun.getSpecifications().getSupportedAmmo().iterator().next());
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), ammoType.createItem(ammoType.getCapacity()));
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();

        if (item != null && GunFeature.getInstance().getConfig().isGunMaterial(item.getType())) {
            event.setCancelled(true);
            // Loading 'item' would result in item desync in Gun
            Gun gun = Gun.loadGun(event.getPlayer().getInventory().getItemInMainHand());
            gun.load(UltimateGuns.getInstance().getGunPlayer(event.getPlayer()));
        }
    }
}
