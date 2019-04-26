package com.samcymbaluk.ultimateguns.features;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.grenades.frag.FragFeature;
import com.samcymbaluk.ultimateguns.grenades.gas.GasFeature;

import java.util.HashMap;
import java.util.Map;

public class PluginFeatures {

    private static PluginFeatures instance;

    private Map<String, PluginFeature> features;

    private PluginFeatures() {
        setupFeatures();
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
    public void setupFeatures() {
        features = new HashMap<>();

        FragFeature fragFeature = new FragFeature();
        features.put(fragFeature.getName(), fragFeature);

        GasFeature gasFeature = new GasFeature();
        features.put(gasFeature.getName(), gasFeature);
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
