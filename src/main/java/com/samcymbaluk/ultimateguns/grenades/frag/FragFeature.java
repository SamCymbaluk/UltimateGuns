package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class FragFeature extends PluginFeature<FragFeatureConfig> {

    private final String name = "frag";

    private FragFeatureConfig config;

    private FragListener fragListener;

    public String getName() {
        return name;
    }

    public Class<FragFeatureConfig> configClass() {
        return FragFeatureConfig.class;
    }

    public FragFeatureConfig defaultConfig() {
        System.out.println(new FragFeatureConfig().getExplosionSounds());
        return new FragFeatureConfig();
    }

    public void enable(FragFeatureConfig config) {
        this.config = config;
        fragListener = new FragListener(this);
    }

    public FragFeatureConfig getConfig() {
        return config;
    }
}
