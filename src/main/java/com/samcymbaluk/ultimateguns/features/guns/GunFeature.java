package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.UltimateGunsPlayer;
import com.samcymbaluk.ultimateguns.features.PluginFeature;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GunFeature extends PluginFeature<GunFeatureConfig> {

    public static final String GUN_NBT_KEY = "ultimateguns_gun";

    public static GunFeature getInstance() {
        return instance;
    }

    private static GunFeature instance;

    private final String name = "guns";

    private GunFeatureConfig config;
    private GunListener gunListener;

    public GunFeature() {
        GunFeature.instance = this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<GunFeatureConfig> configClass() {
        return GunFeatureConfig.class;
    }

    @Override
    public GunFeatureConfig defaultConfig() {
        return new GunFeatureConfig();
    }

    @Override
    public void enable(GunFeatureConfig config) {
        this.config = config;
        this.gunListener = new GunListener();
    }

    @Override
    public GunFeatureConfig getConfig() {
        return config;
    }

}
