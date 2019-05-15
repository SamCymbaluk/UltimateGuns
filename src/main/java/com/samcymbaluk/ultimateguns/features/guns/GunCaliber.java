package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import com.samcymbaluk.ultimateguns.config.util.ConfigPotionEffect;
import com.samcymbaluk.ultimateguns.config.util.PostProcessable;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.ProjectileType;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class GunCaliber implements PostProcessable {

    private String id;
    private String name;

    private int amount;
    private ProjectileType projectileType;
    private int damage;
    private double muzzleVelocity;
    private double damageDropoff;
    private double velocityDropoff;
    private double penetration;
    private double armorPenetration;
    private double heatshotMultiplier;

    private ConfigParticle fireParticle = new ConfigParticle(Particle.SMOKE_NORMAL, 4, 0, 0, 0, 0F, null, true);
    private double fireParticleSpacing = 0.5;
    private int fireParticleAmount = 6;

    private double impactParticleLength = 1;
    private int impactParticleAmount = 25;
    private float impactParticleSpread = 0.015F;
    private boolean impactSound = true;

    private double hitParticleLength = 1;
    private int hitParticleAmount = 25;
    private float hitParticleSpread = 0.015F;

    private List<ConfigPotionEffect> hitEffects = Arrays.asList(new ConfigPotionEffect(PotionEffectType.BLINDNESS, 10, 5));

    public GunCaliber(String id, String name, ProjectileType projectileType, int amount, int damage, double muzzleVelocity, double damageDropoff, double velocityDropoff, double penetration, double armorPenetration, double heatshotMultiplier) {
        this.id = id;
        this.name = name;
        this.projectileType = projectileType;
        this.amount = amount;
        this.damage = damage;
        this.muzzleVelocity = muzzleVelocity;
        this.damageDropoff = damageDropoff;
        this.velocityDropoff = velocityDropoff;
        this.penetration = penetration;
        this.armorPenetration = armorPenetration;
        this.heatshotMultiplier = heatshotMultiplier;
    }

    public GunCaliber(String id, String name, int amount, ProjectileType projectileType, int damage, double muzzleVelocity, double damageDropoff, double velocityDropoff, double penetration, double armorPenetration, double heatshotMultiplier, ConfigParticle fireParticle, double fireParticleSpacing, int fireParticleAmount, double impactParticleLength, int impactParticleAmount, float impactParticleSpread, boolean impactSound, double hitParticleLength, int hitParticleAmount, float hitParticleSpread) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.projectileType = projectileType;
        this.damage = damage;
        this.muzzleVelocity = muzzleVelocity;
        this.damageDropoff = damageDropoff;
        this.velocityDropoff = velocityDropoff;
        this.penetration = penetration;
        this.armorPenetration = armorPenetration;
        this.heatshotMultiplier = heatshotMultiplier;
        this.fireParticle = fireParticle;
        this.fireParticleSpacing = fireParticleSpacing;
        this.fireParticleAmount = fireParticleAmount;
        this.impactParticleLength = impactParticleLength;
        this.impactParticleAmount = impactParticleAmount;
        this.impactParticleSpread = impactParticleSpread;
        this.impactSound = impactSound;
        this.hitParticleLength = hitParticleLength;
        this.hitParticleAmount = hitParticleAmount;
        this.hitParticleSpread = hitParticleSpread;
    }

    public GunCaliber(String id, String name, int amount, ProjectileType projectileType, int damage, double muzzleVelocity, double damageDropoff, double velocityDropoff, double penetration, double armorPenetration, double heatshotMultiplier, ConfigParticle fireParticle, double fireParticleSpacing, int fireParticleAmount, double impactParticleLength, int impactParticleAmount, float impactParticleSpread, boolean impactSound, double hitParticleLength, int hitParticleAmount, float hitParticleSpread, List<ConfigPotionEffect> hitEffects) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.projectileType = projectileType;
        this.damage = damage;
        this.muzzleVelocity = muzzleVelocity;
        this.damageDropoff = damageDropoff;
        this.velocityDropoff = velocityDropoff;
        this.penetration = penetration;
        this.armorPenetration = armorPenetration;
        this.heatshotMultiplier = heatshotMultiplier;
        this.fireParticle = fireParticle;
        this.fireParticleSpacing = fireParticleSpacing;
        this.fireParticleAmount = fireParticleAmount;
        this.impactParticleLength = impactParticleLength;
        this.impactParticleAmount = impactParticleAmount;
        this.impactParticleSpread = impactParticleSpread;
        this.impactSound = impactSound;
        this.hitParticleLength = hitParticleLength;
        this.hitParticleAmount = hitParticleAmount;
        this.hitParticleSpread = hitParticleSpread;
        this.hitEffects = hitEffects;
    }

    @Override
    public void gsonPostProcess() {
        name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public int calcDamage(double distance, double velocity) {
        return (int) Math.round((damage - (damageDropoff * distance)) / (velocityDropoff * (muzzleVelocity / velocity)));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public int getAmount() {
        return amount;
    }

    public int getDamage() {
        return damage;
    }

    public double getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public double getDamageDropoff() {
        return damageDropoff;
    }

    public double getVelocityDropoff() {
        return velocityDropoff;
    }

    public double getPenetration() {
        return penetration;
    }

    public double getArmorPenetration() {
        return armorPenetration;
    }

    public double getHeatshotMultiplier() {
        return heatshotMultiplier;
    }

    public ConfigParticle getFireParticle() {
        return fireParticle;
    }

    public double getFireParticleSpacing() {
        return fireParticleSpacing;
    }

    public int getFireParticleAmount() {
        return fireParticleAmount;
    }

    public double getImpactParticleLength() {
        return impactParticleLength;
    }

    public int getImpactParticleAmount() {
        return impactParticleAmount;
    }

    public float getImpactParticleSpread() {
        return impactParticleSpread;
    }

    public boolean hasImpactSound() {
        return impactSound;
    }

    public double getHitParticleLength() {
        return hitParticleLength;
    }

    public int getHitParticleAmount() {
        return hitParticleAmount;
    }

    public float getHitParticleSpread() {
        return hitParticleSpread;
    }

    public List<ConfigPotionEffect> getHitEffects() {
        return hitEffects;
    }
}
