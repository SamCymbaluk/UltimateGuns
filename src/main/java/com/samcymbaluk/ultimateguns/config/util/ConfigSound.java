package com.samcymbaluk.ultimateguns.config.util;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ConfigSound {

    public static double SPEED_OF_SOUND = 17.15;

    public static void playAll(Iterable<ConfigSound> sounds, Location loc) {
        playAll(sounds, loc, null);
    }

    public static void playAll(Iterable<ConfigSound> sounds, Location loc, Player target) {
        for (ConfigSound sound : sounds) {
            sound.play(loc, target);
        }
    }

    private String sound;
    private SoundCategory category = SoundCategory.MASTER;
    private float volume;
    private float pitch;
    private boolean realistic = false;
    private boolean targetOnly = false;

    public ConfigSound(Sound sound, float volume, float pitch) {
        this.sound = sound.toString().toLowerCase().replace("_", ".");;
        this.volume = volume;
        this.pitch = pitch;
    }

    public ConfigSound(String sound, SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    public ConfigSound(Sound sound, float volume, float pitch, boolean realistic, boolean targetOnly) {
        this.sound = sound.toString().toLowerCase().replace("_", ".");
        this.volume = volume;
        this.pitch = pitch;
        this.realistic = realistic;
        this.targetOnly = targetOnly;
    }

    public ConfigSound(String sound, SoundCategory category, float volume, float pitch, boolean realistic, boolean targetOnly) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
        this.realistic = realistic;
        this.targetOnly = targetOnly;
    }

    public String getSound() {
        return sound;
    }

    public SoundCategory getCategory() {
        return category;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isRealistic() {
        return realistic;
    }

    public boolean isTargetOnly() {
        return targetOnly;
    }

    public void play(Location loc) {
        play(loc, null);
    }

    public void play(Location loc, Player target) {
        List<Player> listeners = targetOnly ? Collections.singletonList(target)
                                            : loc.getWorld().getPlayers();

        for (Player listener : listeners) {
            if (realistic) {
                playRealisticSound(loc, listener);
            } else {
                playSound(loc, listener);
            }
        }
    }

    private void playRealisticSound(Location loc, Player listener) {
        if (listener == null) return;
        if (!loc.getWorld().equals(listener.getWorld())) return;

        double distance = loc.distance(listener.getLocation());
        int delay = (int) (distance / SPEED_OF_SOUND);

        Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> playSound(loc, listener), delay);
    }

    private void playSound(Location loc, Player listener) {
        if (listener == null) return;
        listener.playSound(loc, sound, category, volume, pitch);
    }
}
