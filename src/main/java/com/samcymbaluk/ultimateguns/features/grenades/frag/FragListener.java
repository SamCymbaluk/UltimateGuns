package com.samcymbaluk.ultimateguns.features.grenades.frag;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FragListener implements Listener {

    private FragFeature fragFeature;

    public FragListener(FragFeature fragFeature) {
        this.fragFeature = fragFeature;
        UltimateGuns.getInstance().getServer().getPluginManager().registerEvents(this, UltimateGuns.getInstance());
    }

    @EventHandler
    public void onPlayerIteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!fragFeature.isEnabled(player.getWorld())) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == fragFeature.getConfig().getItemMaterial()) {
                event.setCancelled(true);
                FragGrenade frag = new FragGrenade(fragFeature, player);
                frag.throwGrenade();
            }
        }
    }
}
