package com.samcymbaluk.ultimateguns.features;

import com.samcymbaluk.ultimateguns.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.grenades.frag.FragFeature;
import com.samcymbaluk.ultimateguns.grenades.gas.GasFeature;

import java.util.HashMap;
import java.util.Map;

public class PluginFeatures {

    private static PluginFeatures instance;

    private Map<String, PluginFeature> features = new HashMap<>();

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
    private void setupFeatures() {
        features.put("", null);
    }

    /**
     * TODO
     * @param config
     */
    public void enableFeatures(UltimateGunsConfig config) {
        PluginFeature fragFeature = new FragFeature();
        fragFeature.enable("*");

        PluginFeature gasFeature = new GasFeature();
        gasFeature.enable("*");
    }



}
