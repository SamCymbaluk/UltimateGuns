package com.samcymbaluk.ultimateguns.config.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ConfigParticle implements PostProcessable {

    public static void spawnAll(Iterable<ConfigParticle> particles, Location loc) {
        for (ConfigParticle particle : particles) {
            particle.spawn(loc);
        }
    }

    private Particle particle;
    private int count;
    private double rx;
    private double ry;
    private double rz;
    private double extra;
    private Object data;
    private boolean forceDisplay;

    @Override
    public void gsonPostProcess() {
        Validate.notNull(particle, "Invalid particle name");
    }

    public ConfigParticle(Particle particle, int count, double rx, double ry, double rz, double extra, Object data, boolean forceDisplay) {
        this.particle = particle;
        this.count = count;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.extra = extra;
        this.data = data;
        this.forceDisplay = forceDisplay;
    }

    public Particle getParticle() {
        return particle;
    }

    public int getCount() {
        return count;
    }

    public double getRx() {
        return rx;
    }

    public double getRy() {
        return ry;
    }

    public double getRz() {
        return rz;
    }

    public double getExtra() {
        return extra;
    }

    public Object getData() {
        return data;
    }

    public boolean getForceDisplay() {
        return forceDisplay;
    }

    public void spawn(Location loc) {
        loc.getWorld().spawnParticle(particle, loc, count, rx, ry, rz, extra, data, forceDisplay);
    }
}
