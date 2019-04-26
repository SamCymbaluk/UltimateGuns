package com.samcymbaluk.ultimateguns.grenades.gas;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.features.FeatureConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class GasFeature extends PluginFeature {

    private final String name = "gas";

    private UltimateGunsConfig config;

    private GasManager gasManager;
    private GasListener gasListener;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FeatureConfig getConfig() {
        return new FeatureConfig();
    }

    @Override
    public void enable(UltimateGunsConfig config) {
        this.config = config;
        this.gasManager = new GasManager(this);
        this.gasManager.start();

        this.gasListener = new GasListener(this, this.gasManager);
    }
}
