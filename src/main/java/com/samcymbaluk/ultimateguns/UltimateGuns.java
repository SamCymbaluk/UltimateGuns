package com.samcymbaluk.ultimateguns;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samcymbaluk.ultimateguns.config.ConfigLoader;
import com.samcymbaluk.ultimateguns.config.UltimateGunsConfig;
import com.samcymbaluk.ultimateguns.environment.EnvironmentConfig;
import com.samcymbaluk.ultimateguns.features.PluginFeatures;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UltimateGuns extends JavaPlugin {

    private static UltimateGuns instance;

    private ProtocolManager protocolManager;
    private UltimateGunsConfig config;
    private EnvironmentConfig environmentConfig;

    long tick = 0;
    private PlayerListener playerListener;
    private Map<UUID, UltimateGunsPlayer> gunPlayers = new HashMap<>();

    public static UltimateGuns getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();

        File dataFolder = this.getDataFolder();
        dataFolder.mkdir();
        loadEnvironmentConfig();
        playerListener = new PlayerListener();
        tickTask();

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

    private void tickTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (UltimateGunsPlayer gunsPlayer : gunPlayers.values()) {
                gunsPlayer.updateLoc();
            }
            tick++;
        }, 1, 1);
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

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public EnvironmentConfig getEnvironmentConfig() {
        return environmentConfig;
    }

    public UltimateGunsPlayer getGunPlayer(Player p) {
        return gunPlayers.get(p.getUniqueId());
    }

    public long getTick() {
        return tick;
    }

    void addGunPlayer(Player p) {
        gunPlayers.put(p.getUniqueId(), new UltimateGunsPlayer(p));
    }

    void removeGunPlayer(Player p) {
        gunPlayers.remove(p.getUniqueId());
    }

    @Override
    public void onDisable() {
        getLogger().info("UltimateGuns onDisable");
    }

}
