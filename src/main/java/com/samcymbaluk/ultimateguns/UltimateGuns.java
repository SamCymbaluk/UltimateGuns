package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.config.UltimateGunsConfigLoader;
import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeatures;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class UltimateGuns extends JavaPlugin {

    private static UltimateGuns instance;
    private UltimateGunsConfig config;

    public static UltimateGuns getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        loadFeatures();
    }

    private void loadFeatures() {
        UltimateGunsConfigLoader configLoader = new UltimateGunsConfigLoader();
        PluginFeatures.getInstance().setupFeatures(configLoader);
        configLoader.build();

        try {
            File dataFolder = this.getDataFolder();
            dataFolder.mkdir();

            File configFile = new File(this.getDataFolder().getPath() + File.separator + "config.json");
            config = configLoader.loadConfig(UltimateGunsConfig.class, configFile, new UltimateGunsConfig());
        } catch (IOException ex) {
            ex.printStackTrace();
            this.getLogger().severe("Error loading config. Falling back to defaults");
            config = new UltimateGunsConfig();
        }

        PluginFeatures.getInstance().enableFeatures(config);
    }

    @Override
    public void onDisable() {
        getLogger().info("UltimateGuns onDisable");
    }

}
