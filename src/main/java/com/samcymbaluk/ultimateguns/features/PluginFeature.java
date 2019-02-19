package com.samcymbaluk.ultimateguns.features;

import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public abstract class PluginFeature {

    private Set<String> enabledWorlds = new HashSet<>();

    public boolean isEnabled() {
        return !enabledWorlds.isEmpty();
    }

    public boolean isEnabled(String world) {
        return enabledWorlds.contains("*") || enabledWorlds.contains(world);
    }

    public boolean isEnabled(World world) {
        return enabledWorlds.contains("*") || enabledWorlds.contains(world.getName());
    }

    public void enable(String world) {
        enabledWorlds.add(world);
    }

    public void enable(World world) {
        enabledWorlds.add(world.getName());
    }

    public void disable(String world) {
        enabledWorlds.remove(world);
    }

    public void disable(World world) {
        enabledWorlds.remove(world.getName());
    }

    public void onPluginDisable() {

    }
}
