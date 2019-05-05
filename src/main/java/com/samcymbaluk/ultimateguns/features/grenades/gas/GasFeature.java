package com.samcymbaluk.ultimateguns.features.grenades.gas;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class GasFeature extends PluginFeature<GasFeatureConfig> {

    private final String name = "gas";

    private GasFeatureConfig config;

    private GasManager gasManager;
    private GasListener gasListener;

    public String getName() {
        return name;
    }

    public Class<GasFeatureConfig> configClass() {
        return GasFeatureConfig.class;
    }

    public GasFeatureConfig defaultConfig() {
        return new GasFeatureConfig();
    }

    public void enable(GasFeatureConfig config) {
        this.config = config;
        this.gasManager = new GasManager(this);
        this.gasManager.start();

        this.gasListener = new GasListener(this, this.gasManager);
    }

    public GasFeatureConfig getConfig() {
        return this.config;
    }
}
