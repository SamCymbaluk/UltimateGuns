package com.samcymbaluk.ultimateguns.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtil {

    public static void safeAdd(Player player, ItemStack item) {
        // No room for mag in inventory
        ItemStack extra = player.getInventory().addItem(item).get(0);
        if (extra != null) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), extra);
        }
    }
}
