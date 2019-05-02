package com.samcymbaluk.ultimateguns.features;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.config.ConfigLoader;
import com.samcymbaluk.ultimateguns.grenades.frag.FragFeature;
import com.samcymbaluk.ultimateguns.grenades.gas.GasFeature;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginFeatures {

    public static String DIR_NAME = "features";

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
    public void setupFeatures() {
        features = new HashMap<>();

        setupFeature(new FragFeature());
        setupFeature(new GasFeature());
    }

    private void setupFeature(PluginFeature feature) {
        features.put(feature.getName(), feature);
    }

    public  Map<String, PluginFeature> getFeatures() {
        return features;
    }

    public void enableFeatures() {
        File featureDir = new File(UltimateGuns.getInstance().getDataFolder().getPath() + File.separator + DIR_NAME);
        featureDir.mkdir();

        for (Map.Entry<String, PluginFeature> entry : features.entrySet()) {
            String name = entry.getKey();
            PluginFeature feature = entry.getValue();

            try {
                File featureConfigFile = new File(UltimateGuns.getInstance().getDataFolder().getPath() + File.separator
                        + DIR_NAME
                        + File.separator + name + ".json");

                // This all works, but the typing is a little strange. Consider getting someone more versed in the Java
                // type system to review it at a future date
                feature.enable(ConfigLoader.loadConfig(feature.configClass(), featureConfigFile, feature.defaultConfig()));
            } catch (IOException ex) {
                ex.printStackTrace();
                UltimateGuns.getInstance().getLogger().severe(String.format("Unable to load config for \"%s\" feature", name));
                continue;
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                UltimateGuns.getInstance().getLogger().severe(String.format("Unable to load config for \"%s\" feature", name));
                UltimateGuns.getInstance().getLogger().severe("Ensure the feature configuration is properly formatted");
            }

            // TODO
            feature.enableInWorlds(Arrays.asList("*"));

        }
    }
}
