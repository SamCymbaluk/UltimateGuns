package com.samcymbaluk.ultimateguns.grenades;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Grenade {

    private Player thrower;
    private GrenadeProjectile projectile;

    private double startVelocity;
    private Material material;

    public Grenade(Player thrower, double startVelocity, Material material) {
        this.thrower = thrower;
        this.startVelocity = startVelocity;
        this.material = material;
    }

    public Player getThrower() {
        return thrower;
    }

    public GrenadeProjectile getProjectile() {
        return projectile;
    }

    public void throwGrenade() {
        projectile = new GrenadeProjectile(thrower, thrower.getEyeLocation(), this, material);
        projectile.start(thrower.getEyeLocation(), startVelocity);


        ItemStack item = thrower.getInventory().getItemInMainHand();

        if (item.getAmount() == 1) {
            thrower.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));
        } else {
            item.setAmount(item.getAmount() - 1);
            thrower.getInventory().setItemInMainHand(item);
        }

    }

    public abstract boolean onImpact(Vector path, Location loc);

    public abstract void onTick(Location loc, int tick);
}
