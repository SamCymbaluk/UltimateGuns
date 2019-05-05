package com.samcymbaluk.ultimateguns.features.guns;

public class GunCaliber {

    private String id;
    private String name;

    private int damage;
    private double muzzleVelocity;
    private double damageDropoff;
    private double velocityDropoff;
    private double penetration;
    private double armorPenetration;
    private double heatshotMultiplier;

    public GunCaliber(String id, String name, int damage, double muzzleVelocity, double damageDropoff, double velocityDropoff, double penetration, double armorPenetration, double heatshotMultiplier) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.muzzleVelocity = muzzleVelocity;
        this.damageDropoff = damageDropoff;
        this.velocityDropoff = velocityDropoff;
        this.penetration = penetration;
        this.armorPenetration = armorPenetration;
        this.heatshotMultiplier = heatshotMultiplier;
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
