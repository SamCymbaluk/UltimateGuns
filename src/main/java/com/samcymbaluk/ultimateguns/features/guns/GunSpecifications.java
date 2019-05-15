package com.samcymbaluk.ultimateguns.features.guns;

import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Set;

public class GunSpecifications {

    private String id;
    private Material material;

    private String gunName = "&l&7Gun";
    private String ammoString = " &7<&e@&7>";

    private int roundsPerBurst = 1;
    private int bulletDelay = 1;
    private boolean auto = true;

    private double accuracy = 0;
    private double movingAccuracy = 0;
    private double jumpingAccuracy = 0;

    private int reloadTime = 20;

    private int individualAmmoCapacity = 1;

    private Set<String> supportedAmmo;

    public GunSpecifications(String id, Material material, Set<String> supportedAmmo) {
        this.id = id;
        this.material = material;
        this.supportedAmmo = Sets.newHashSet(supportedAmmo);
    }

    public GunSpecifications(String id, Material material, String gunName, int roundsPerBurst, int bulletDelay, boolean auto, double accuracy, double movingAccuracy, double jumpingAccuracy, int reloadTime, int individualAmmoCapacity, Set<String> supportedAmmo) {
        this.id = id;
        this.material = material;
        this.gunName = gunName;
        this.roundsPerBurst = roundsPerBurst;
        this.bulletDelay = bulletDelay;
        this.auto = auto;
        this.accuracy = accuracy;
        this.movingAccuracy = movingAccuracy;
        this.jumpingAccuracy = jumpingAccuracy;
        this.reloadTime = reloadTime;
        this.individualAmmoCapacity = individualAmmoCapacity;
        this.supportedAmmo = Sets.newHashSet(supportedAmmo);
    }

    public String getAmmoString(int currentAmmo, boolean loaded) {
        String str = ChatColor.translateAlternateColorCodes('&', ammoString);

        String ammoReplacement = loaded ? Integer.toString(currentAmmo) : "-";
        str = str.replace(GunFeature.getInstance().getConfig().getCurrentAmmoPlaceholder(), ammoReplacement);
        return str;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getGunName() {
        return ChatColor.translateAlternateColorCodes('&', gunName);
    }

    public int getRoundsPerBurst() {
        return roundsPerBurst;
    }

    public int getBulletDelay() {
        return bulletDelay;
    }

    public boolean isAuto() {
        return auto;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getMovingAccuracy() {
        return movingAccuracy;
    }

    public double getJumpingAccuracy() {
        return jumpingAccuracy;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public int getIndividualAmmoCapacity() {
        return individualAmmoCapacity;
    }

    public Set<String> getSupportedAmmo() {
        return supportedAmmo;
    }

    public boolean isSupported(String ammo) {
        return supportedAmmo.contains(ammo);
    }

    public boolean isSupported(GunAmmo ammo) {
        return supportedAmmo.contains(ammo.getId());
    }

}
