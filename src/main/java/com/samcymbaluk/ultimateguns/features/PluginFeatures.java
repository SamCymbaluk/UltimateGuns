package com.samcymbaluk.ultimateguns.features;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.config.UltimateGunsConfigLoader;
import com.samcymbaluk.ultimateguns.grenades.frag.FragFeature;
import com.samcymbaluk.ultimateguns.grenades.gas.GasFeature;

import java.util.HashMap;
import java.util.Map;

public class PluginFeatures {

    private static PluginFeatures instance;

    private Map<String, PluginFeature> features;

    private PluginFeatures() {
    }

    public synchronized static PluginFeatures getInstance() {
        if (instance == null) {
            instance = new PluginFeatures();
        }
        return instance;
    }

    /**
     * TODO
     */
    public void setupFeatures(UltimateGunsConfigLoader configLoader) {
        features = new HashMap<>();

        setupFeature(new FragFeature(), configLoader);
        setupFeature(new GasFeature(), configLoader);
    }

    private void setupFeature(PluginFeature feature, UltimateGunsConfigLoader configLoader) {
        features.put(feature.getName(), feature);
        configLoader.registerFeatureConfig(feature.getDefaultConfig());
    }

    public  Map<String, PluginFeature> getFeatures() {
        return features;
    };


    /**
     * TODO
     * @param config
     */
    public void enableFeatures(UltimateGunsConfig config) {
        for (Map.Entry<String, PluginFeature> entry : features.entrySet()) {
            entry.getValue().enableInWorlds(config.getFeatureConfig(entry.getKey()).getEnabledWorlds());
            entry.getValue().enable(config);
        }
    }



}
