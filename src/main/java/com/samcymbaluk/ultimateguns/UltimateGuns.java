package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.features.PluginFeatures;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGuns extends JavaPlugin {

    private static UltimateGuns instance;
    private UltimateGunsConfig config;

    public static UltimateGuns getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // TODO Load config
        config = new UltimateGunsConfig();

        PluginFeatures.getInstance().enableFeatures(config);
    }

    @Override
    public void onDisable() {
        getLogger().info("UltimateGuns onDisable");
    }

}
