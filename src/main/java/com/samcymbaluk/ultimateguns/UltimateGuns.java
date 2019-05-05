package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.config.ConfigLoader;
import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.environment.EnvironmentConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeature;
import com.samcymbaluk.ultimateguns.features.PluginFeatures;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class UltimateGuns extends JavaPlugin {

    private static UltimateGuns instance;
    private UltimateGunsConfig config;

    private EnvironmentConfig environmentConfig;

    public static UltimateGuns getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        File dataFolder = this.getDataFolder();
        dataFolder.mkdir();

        loadEnvironmentConfig();
        loadFeatures();
    }

    private void loadEnvironmentConfig() {
        try {
            File environmentConfigFile = new File(this.getDataFolder().getPath() + File.separator + "environment.json");
            environmentConfig = ConfigLoader.loadConfig(EnvironmentConfig.class, environmentConfigFile, new EnvironmentConfig());
        } catch (IOException ex) {
            ex.printStackTrace();
            this.getLogger().severe("Error loading environment config. Falling back to defaults");
            environmentConfig = new EnvironmentConfig();
        }
    }

    private void loadFeatures() {
        PluginFeatures.getInstance().setupFeatures();

        try {
            File configFile = new File(this.getDataFolder().getPath() + File.separator + "config.json");
            config = ConfigLoader.loadConfig(UltimateGunsConfig.class, configFile, new UltimateGunsConfig());
        } catch (IOException ex) {
            ex.printStackTrace();
            this.getLogger().severe("Error loading config. Falling back to defaults");
            config = new UltimateGunsConfig();
        }

        PluginFeatures.getInstance().enableFeatures();
    }

    public EnvironmentConfig getEnvironmentConfig() {
        return environmentConfig;
    }

    @Override
    public void onDisable() {
        getLogger().info("UltimateGuns onDisable");
    }

}
