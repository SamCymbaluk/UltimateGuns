package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.features.FeatureConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class FragFeature extends PluginFeature {

    private final String name = "frag";

    private UltimateGunsConfig config;

    private FragListener fragListener;

    public String getName() {
        return name;
    }

    public FeatureConfig getConfig() {
        return new FeatureConfig();
    }

    public void enable(UltimateGunsConfig config) {
        this.config = config;
        fragListener = new FragListener(this);
    }
}
