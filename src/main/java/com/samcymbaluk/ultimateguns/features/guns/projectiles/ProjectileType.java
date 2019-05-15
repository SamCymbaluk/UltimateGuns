package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.features.guns.Gun;
import com.samcymbaluk.ultimateguns.features.guns.GunCaliber;
import org.bukkit.entity.Player;

public enum ProjectileType {

    BULLET() {
        @Override
        public GunProjectile getProjectile(Gun gun, GunCaliber caliber, Player owner) {
            return new Bullet(gun, caliber, owner);
        }
    },
    ROCKET;

    public GunProjectile getProjectile(Gun gun, GunCaliber caliber, Player owner) {
        return null;
    }
}
