package com.samcymbaluk.ultimateguns.grenades;

import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import org.bukkit.Sound;

public class GrenadeFeatureConfig {

    private ConfigSound impactSound = new ConfigSound(Sound.BLOCK_ANVIL_PLACE, 1, 1);

    private double gravity = 0.045;

    private double liquidMultiplier = 0.5;

    public ConfigSound getImpactSound() {
        return impactSound;
    }

    public double getGravity() {
        return gravity;
    }

    public double getLiquidMultiplier() {
        return liquidMultiplier;
    }
}
