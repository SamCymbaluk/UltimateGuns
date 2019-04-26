package com.samcymbaluk.ultimateguns.config;

import com.samcymbaluk.ultimateguns.features.FeatureConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeature;
import com.samcymbaluk.ultimateguns.features.PluginFeatures;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class UltimateGunsConfig {

    private String prefix = GRAY + "" + BOLD + "[" + YELLOW + "" + BOLD + "UltimateGuns" + GRAY + "" + BOLD + "] ";

    private Map<String, FeatureConfig> features = new LinkedHashMap<>();

    public UltimateGunsConfig() {
        for (Map.Entry<String, PluginFeature> entry : PluginFeatures.getInstance().getFeatures().entrySet()) {
            features.put(entry.getKey(), entry.getValue().getConfig());
        }
    }

    public FeatureConfig getFeatureConfig(String feature) {
        return features.get(feature);
    }
}
