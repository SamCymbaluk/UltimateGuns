package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.features.guns.Gun;
import org.bukkit.entity.LivingEntity;

public enum ProjectileType {

    BULLET() {
        @Override
        public GunProjectile getProjectile(Gun gun, LivingEntity owner) {
            return new Bullet(gun, owner);
        }
    },
    ROCKET;

    public GunProjectile getProjectile(Gun gun, LivingEntity owner) {
        return null;
    }
}
