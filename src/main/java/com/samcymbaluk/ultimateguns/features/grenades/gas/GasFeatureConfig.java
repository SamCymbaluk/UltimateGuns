package com.samcymbaluk.ultimateguns.features.grenades.gas;

import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import com.samcymbaluk.ultimateguns.config.util.ConfigPotionEffect;
import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import com.samcymbaluk.ultimateguns.config.util.PostProcessable;
import com.samcymbaluk.ultimateguns.features.grenades.GrenadeFeatureConfig;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GasFeatureConfig extends GrenadeFeatureConfig implements PostProcessable {

    private int fuse = 80;
    private int throwVelocity = 25;
    private Material itemMaterial = Material.IRON_INGOT;
    private Material entityMaterial = Material.IRON_BLOCK;

    private int releaseDuration = 120;
    private int releasePeriod = 1;
    private int releaseAmount = 6;
    private List<ConfigSound> releaseSounds = Arrays.asList(
            new ConfigSound(Sound.ENTITY_CREEPER_PRIMED, 1, 2),
            new ConfigSound(Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.5F)
    );

    private ConfigParticle gasParticle = new ConfigParticle(Particle.SPELL_MOB, 0, 19.0 / 255.0, 219.0  / 255.0, 73.0 / 255.0, 1, null, false);
    private double particleFrequency = 0.2;
    private double particleDensityMultiplier = 0.25;
    private int maxParticleAmount = 50;

    private int effectPollPeriod = 5;
    private List<ConfigPotionEffect> effects = Arrays.asList(
            new ConfigPotionEffect(PotionEffectType.CONFUSION, 200, 3),
            new ConfigPotionEffect(PotionEffectType.WITHER, 100, 2)
    );
    private boolean affectMobs = true;

    private Set<EntityType> ignoredEntities = new HashSet<>(Arrays.asList(
            EntityType.WITHER_SKELETON,
            EntityType.SKELETON,
            EntityType.GHAST,
            EntityType.BLAZE,
            EntityType.WITHER,
            EntityType.SHULKER,
            EntityType.IRON_GOLEM,
            EntityType.PHANTOM,
            EntityType.SKELETON_HORSE
    ));

    private boolean gasGravity = true;
    private int gasGravityPeriod = 100;

    private boolean extinguishFire = true;

    private int dissipationPollPeriod = 20;
    private double dissipationChance = 0.1;

    @Override
    public void gsonPostProcess() {
        Validate.noNullElements(ignoredEntities, "Invalid entity type");
    }

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

    public int getReleaseDuration() {
        return releaseDuration;
    }

    public int getReleasePeriod() {
        return releasePeriod;
    }

    public int getReleaseAmount() {
        return releaseAmount;
    }

    public List<ConfigSound> getReleaseSounds() {
        return releaseSounds;
    }

    public ConfigParticle getGasParticle() {
        return gasParticle;
    }

    public double getParticleFrequency() {
        return particleFrequency;
    }

    public double getParticleDensityMultiplier() {
        return particleDensityMultiplier;
    }

    public int getMaxParticleAmount() {
        return maxParticleAmount;
    }

    public int getEffectPollPeriod() {
        return effectPollPeriod;
    }

    public List<ConfigPotionEffect> getEffects() {
        return effects;
    }

    public boolean isAffectMobs() {
        return affectMobs;
    }

    public Set<EntityType> getIgnoredEntities() {
        return ignoredEntities;
    }

    public boolean hasGasGravity() {
        return gasGravity;
    }

    public int getGasGravityPeriod() {
        return gasGravityPeriod;
    }

    public boolean isExtinguishFire() {
        return extinguishFire;
    }

    public int getDissipationPollPeriod() {
        return dissipationPollPeriod;
    }

    public double getDissipationChance() {
        return dissipationChance;
    }
}
