package com.samcymbaluk.ultimateguns.config;

import com.samcymbaluk.ultimateguns.features.FeatureConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeature;
import com.samcymbaluk.ultimateguns.features.PluginFeatures;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class UltimateGunsConfig {

    private String prefix = GRAY + "" + BOLD + "[" + YELLOW + "" + BOLD + "UltimateGuns" + GRAY + "" + BOLD + "] ";

    private Map<String, Object> features;

    public UltimateGunsConfig() {
        features = new LinkedHashMap<>();

        for (Map.Entry<String, PluginFeature> entry : PluginFeatures.getInstance().getFeatures().entrySet()) {
            features.put(entry.getKey(), entry.getValue().getDefaultConfig());
        }
        PluginFeatures.getInstance().enableFeatures(this);
    }
    
    public FeatureConfig getFeatureConfig(String feature) {
        return (FeatureConfig) features.get(feature);
    }
}
