package com.samcymbaluk.ultimateguns.features.guns;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GunSpecifications {

    private String gunName = "&l&7Gun";
    private String ammoString = " &7<&e@&7:&e#&7>";
    private String currentAmmoChar = "@";
    private String maxAmmoChar = "#";

    private Material ammoType = Material.SLIME_BALL;
    private String ammoName = "&7Mag";

    private int roundsPerBurst = 1;
    private int bulletDelay = 1;
    private boolean auto = true;

    private double accuracy = 0;
    private double movingAccuracy = 0;
    private double jumpingAccuracy = 0;

    private int ammoSize = 30;
    private int reloadTime = 20;

    private double smokeSpacing = 1;
    private int smokeSteps = 5;

    private String caliber;

    public GunSpecifications(String caliber) {
        this.caliber = caliber;
    }

    public GunSpecifications(String gunName, Material ammoType, String ammoName, int roundsPerBurst, int bulletDelay, boolean auto, double accuracy, double movingAccuracy, double jumpingAccuracy, int ammoSize, int reloadTime, double smokeSpacing, int smokeSteps, String caliber) {
        this.gunName = gunName;
        this.ammoType = ammoType;
        this.ammoName = ammoName;
        this.roundsPerBurst = roundsPerBurst;
        this.bulletDelay = bulletDelay;
        this.auto = auto;
        this.accuracy = accuracy;
        this.movingAccuracy = movingAccuracy;
        this.jumpingAccuracy = jumpingAccuracy;
        this.ammoSize = ammoSize;
        this.reloadTime = reloadTime;
        this.smokeSpacing = smokeSpacing;
        this.smokeSteps = smokeSteps;
        this.caliber = caliber;
    }

    public String getAmmoString(int currentAmmo) {
        String str = ChatColor.translateAlternateColorCodes('&', ammoString);
        str = str.replace(currentAmmoChar, Integer.toString(currentAmmo));
        str = str.replace(maxAmmoChar, Integer.toString(ammoSize));
        return str;
    }


    public String getGunName() {
        return ChatColor.translateAlternateColorCodes('&', gunName);
    }

    public Material getAmmoType() {
        return ammoType;
    }

    public String getAmmoName() {
        return ChatColor.translateAlternateColorCodes('&', ammoName);
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

    public int getAmmoSize() {
        return ammoSize;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public double getSmokeSpacing() {
        return smokeSpacing;
    }

    public int getSmokeSteps() {
        return smokeSteps;
    }

    public String getCaliber() {
        return caliber;
    }
}
