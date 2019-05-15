package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.features.guns.Gun;
import com.samcymbaluk.ultimateguns.features.guns.GunCaliber;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

abstract public class GunProjectile {

    private Gun gun;
    private Player owner;
    private GunCaliber caliber;

    public GunProjectile(Gun gun, GunCaliber caliber, Player owner) {
        this.gun = gun;
        this.caliber = caliber;
        this.owner = owner;
    }

    public abstract void fire();

    public Gun getGun() {
        return gun;
    }

    public GunCaliber getCaliber() {
        return caliber;
    }

    public Player getOwner() {
        return owner;
    }

    public void shotEffect(Location start, Vector path) {
        Location pos = start.clone();
        Vector add = path.clone().normalize().multiply(caliber.getFireParticleSpacing());

        for (int i = 0; i < caliber.getFireParticleAmount(); i++) {
            pos.add(add);
            caliber.getFireParticle().spawn(pos);

            // Prevent particles from going through walls
            if(!pos.getBlock().isPassable()) break;
        }
    }
}
