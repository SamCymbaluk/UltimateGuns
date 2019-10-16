package com.samcymbaluk.ultimateguns.features.shield;

import com.samcymbaluk.ultimateguns.features.PluginFeature;
import com.samcymbaluk.ultimateguns.targets.Target;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ShieldFeature extends PluginFeature<ShieldFeatureConfig> {

    public static ShieldFeature getInstance() {
        return instance;
    }

    private static ShieldFeature instance;

    private final String name = "shield";

    private ShieldFeatureConfig config;
    private ShieldListener shieldListener;

    private Set<ShieldTarget> shieldTargets = new HashSet<>();

    public ShieldFeature() {
        ShieldFeature.instance = this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<ShieldFeatureConfig> configClass() {
        return ShieldFeatureConfig.class;
    }

    @Override
    public ShieldFeatureConfig defaultConfig() {
        return new ShieldFeatureConfig();
    }

    @Override
    public void enable(ShieldFeatureConfig config) {
        this.config = config;
        this.shieldListener = new ShieldListener();
    }

    @Override
    public ShieldFeatureConfig getConfig() {
        return config;
    }

    public void addShieldTarget(Player player) {
        ShieldTarget target = new ShieldTarget(player);
        shieldTargets.add(target);
        Target.registerCustomTarget(target);
    }

    public void removeShieldTarget(Player player) {
        ShieldTarget target = new ShieldTarget(player);
        shieldTargets.remove(target);
        Target.removeCustomTarget(target);
    }
}
