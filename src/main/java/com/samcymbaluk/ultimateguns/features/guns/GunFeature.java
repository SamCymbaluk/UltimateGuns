package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class GunFeature extends PluginFeature<GunFeatureConfig> {

    private final String name = "guns";

    private GunFeatureConfig config;

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
    }

    @Override
    public GunFeatureConfig getConfig() {
        return config;
    }
}
