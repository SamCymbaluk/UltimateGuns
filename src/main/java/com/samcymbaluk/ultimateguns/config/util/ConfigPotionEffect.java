package com.samcymbaluk.ultimateguns.config.util;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConfigPotionEffect implements PostProcessable {

    public static void addAll(Iterable<ConfigPotionEffect> effects, LivingEntity entity, boolean force) {
        for (ConfigPotionEffect effect : effects) {
            effect.add(entity, force);
        }
    }

    private String type;
    private int duration;
    private int amplifier;
    private boolean ambient = false;
    private boolean particles = false;
    private boolean icon = false;

    @Override
    public void gsonPostProcess() {
        Validate.notNull(PotionEffectType.getByName(type), "Invalid potion effect name");
    }

    public ConfigPotionEffect(String type, int duration, int amplifier) {
        this(PotionEffectType.getByName(type), duration, amplifier);
    }

    public ConfigPotionEffect(PotionEffectType type, int duration, int amplifier) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }

        this.type = type.getName();
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public ConfigPotionEffect(String type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        this(PotionEffectType.getByName(type), duration, amplifier, ambient, particles, icon);
    }

    public ConfigPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }

        this.type = type.toString();
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
        this.icon = icon;
    }

    public PotionEffectType getType() {
        return PotionEffectType.getByName(type);
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean isAmbient() {
        return ambient;
    }

    public boolean isParticles() {
        return particles;
    }

    public boolean isIcon() {
        return icon;
    }

    public PotionEffect toPotionEffect() {
        return new PotionEffect(getType(), duration, amplifier, ambient, particles, icon);
    }

    public boolean add(LivingEntity entity, boolean force) {
        return entity.addPotionEffect(toPotionEffect(), force);
    }
}
