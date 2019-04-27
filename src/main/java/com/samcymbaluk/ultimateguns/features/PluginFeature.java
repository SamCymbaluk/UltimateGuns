package com.samcymbaluk.ultimateguns.features;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import org.bukkit.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PluginFeature {

    private Set<String> enabledWorlds = new HashSet<>();

    public abstract String getName();

    public abstract FeatureConfig getDefaultConfig();

    public abstract void enable(UltimateGunsConfig config);

    public boolean isEnabled() {
        return !enabledWorlds.isEmpty();
    }

    public boolean isEnabled(String world) {
        return enabledWorlds.contains("*") || enabledWorlds.contains(world);
    }

    public boolean isEnabled(World world) {
        return enabledWorlds.contains("*") || enabledWorlds.contains(world.getName());
    }

    public void enableInWorld(String world) {
        enabledWorlds.add(world);
    }

    public void enableInWorld(World world) {
        enabledWorlds.add(world.getName());
    }

    public void enableInWorlds(List<String> worlds) {
        enabledWorlds.addAll(worlds);
    }

    public void disableInWorld(String world) {
        enabledWorlds.remove(world);
    }

    public void disableInWorld(World world) {
        enabledWorlds.remove(world.getName());
    }

    public void onPluginDisable() {

    }
}
