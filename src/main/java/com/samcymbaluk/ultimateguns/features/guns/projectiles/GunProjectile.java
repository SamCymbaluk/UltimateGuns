package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.features.guns.Gun;
import org.bukkit.entity.LivingEntity;

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
}
