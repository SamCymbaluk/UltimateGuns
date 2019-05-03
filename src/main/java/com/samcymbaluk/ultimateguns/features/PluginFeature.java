package com.samcymbaluk.ultimateguns.features;

import org.bukkit.World;
public abstract class PluginFeature<T extends PluginFeatureConfig> {

    public abstract String getName();

    public abstract Class<T> configClass();

    public abstract T defaultConfig();

    public abstract void enable(T config);

    public abstract T getConfig();

    public boolean isEnabled() {
        return getConfig().getEnabledWorlds().isEmpty();
    }

    public boolean isEnabled(String world) {
        return getConfig().getEnabledWorlds().contains("*") || getConfig().getEnabledWorlds().contains(world);
    }

    public boolean isEnabled(World world) {
        return getConfig().getEnabledWorlds().contains("*") || getConfig().getEnabledWorlds().contains(world.getName());
    }

    public void onPluginDisable() {

    }
}
