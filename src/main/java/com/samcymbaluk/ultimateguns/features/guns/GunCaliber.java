package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.config.util.PostProcessable;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.ProjectileType;
import org.bukkit.ChatColor;

public class GunCaliber implements PostProcessable {

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

    public GunCaliber(String name, ProjectileType projectileType, int amount, int damage, double muzzleVelocity, double damageDropoff, double velocityDropoff, double penetration, double armorPenetration, double heatshotMultiplier) {
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

    @Override
    public void gsonPostProcess() {
        name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public int calcDamage(double distance, double velocity) {
        return (int) Math.round((damage - (damageDropoff * distance)) / (velocityDropoff * (muzzleVelocity / velocity)));
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
}
