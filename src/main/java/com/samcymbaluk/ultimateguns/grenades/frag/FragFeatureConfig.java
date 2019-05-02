package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragFeatureConfig {

    private int fuse = 80;
    private int throwVelocity = 25;
    private Material itemMaterial = Material.COAL;
    private Material entityMaterial = Material.COAL_BLOCK;

    private double initialDamage = 30;
    private double damageDropoff = 2.5;
    private double explosionRadius = 10;

    private List<ConfigParticle> explosionParticles = Arrays.asList(
        new ConfigParticle(Particle.EXPLOSION_HUGE, 1, 0, 0, 0, 0.1, null, true),
        new ConfigParticle(Particle.EXPLOSION_NORMAL, 300, 0.5, 0.5, 0.5, 0.5, null, true),
        new ConfigParticle(Particle.EXPLOSION_NORMAL, 100, 0.5, 0.5, 0.5, 1.0, null, true)
    );

    private List<ConfigSound> explosionSounds = Arrays.asList(
        new ConfigSound(Sound.ENTITY_GENERIC_EXPLODE, 6, 0.75F, true, false),
        new ConfigSound(Sound.ENTITY_GENERIC_EXPLODE, 6, 1, true, false)
    );

    public int getFuse() {
        return fuse;
    }

    public int getThrowVelocity() {
        return throwVelocity;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public Material getEntityMaterial() {
        return entityMaterial;
    }

    public double getInitialDamage() {
        return initialDamage;
    }

    public double getDamageDropoff() {
        return damageDropoff;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public List<ConfigParticle> getExplosionParticles() {
        return explosionParticles;
    }

    public List<ConfigSound> getExplosionSounds() {
        return explosionSounds;
    }
}
