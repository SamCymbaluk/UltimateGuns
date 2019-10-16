package com.samcymbaluk.ultimateguns.features.shield;

import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import com.samcymbaluk.ultimateguns.features.PluginFeatureConfig;
import org.bukkit.Sound;

public class ShieldFeatureConfig extends PluginFeatureConfig {

    private boolean reflect = false;

    private double penetrationCost = 50;
    private double restitution = 0.1;

    private ConfigSound impactSound = new ConfigSound(Sound.ITEM_SHIELD_BLOCK, 1, 1);

    public boolean isReflect() {
        return reflect;
    }

    public double getPenetrationCost() {
        return penetrationCost;
    }

    public double getRestitution() {
        return restitution;
    }

    public ConfigSound getImpactSound() {
        return impactSound;
    }
}
