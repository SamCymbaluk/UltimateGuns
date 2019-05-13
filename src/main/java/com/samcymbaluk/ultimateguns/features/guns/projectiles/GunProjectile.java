package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.features.guns.Gun;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

abstract public class GunProjectile {

    private Gun gun;
    private LivingEntity owner;

    public GunProjectile(Gun gun, LivingEntity owner) {
        this.gun = gun;
        this.owner = owner;
    }

    public abstract void fire();

    public Gun getGun() {
        return gun;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public void shotEffect(Location start, Vector path) {
        Location pos = start.clone();
        Vector add = path.clone().normalize().multiply(getGun().getCaliber().getFireParticleSpacing());

        for (int i = 0; i < getGun().getCaliber().getFireParticleAmount(); i++) {
            pos.add(add);
            getGun().getCaliber().getFireParticle().spawn(pos);

            // Prevent particles from going through walls
            if(!pos.getBlock().isPassable()) break;
        }
    }
}
