package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.config.ConfigLoader;
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

        PluginFeatures.getInstance().setupFeatures();
        loadConfig();
        PluginFeatures.getInstance().enableFeatures(config);
    }

    private void loadConfig() {
        try {
            File dataFolder = this.getDataFolder();
            dataFolder.mkdir();

            File configFile = new File(this.getDataFolder().getPath() + File.separator + "config.json");
            config = ConfigLoader.loadConfig(UltimateGunsConfig.class, configFile, new UltimateGunsConfig());
        } catch (IOException ex) {
            ex.printStackTrace();
            this.getLogger().severe("Error loading config. Falling back to defaults");
            config = new UltimateGunsConfig();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("UltimateGuns onDisable");
    }

}
