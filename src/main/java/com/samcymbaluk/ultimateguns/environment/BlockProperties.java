package com.samcymbaluk.ultimateguns.environment;

import org.bukkit.Material;

public class BlockProperties {

    private double penetrationCost;
    private double restitution;

    private boolean destructible;
    private double destructionThreshold;

    public BlockProperties(double penetrationCost, double restitution, boolean destructible, double destructionThreshold) {
        this.penetrationCost = penetrationCost;
        this.restitution = restitution;
        this.destructible = destructible;
        this.destructionThreshold = destructionThreshold;
    }

    public double getPenetrationCost() {
        return penetrationCost;
    }

    public double getRestitution() {
        return restitution;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public double getDestructionThreshold() {
        return destructionThreshold;
    }
}
