package com.samcymbaluk.ultimateguns.environment;

import org.bukkit.entity.EntityType;

public class EntityProperties {

    private double penetrationCost;
    private double restitution;

    public EntityProperties(double penetrationCost, double restitution) {
        this.penetrationCost = penetrationCost;
        this.restitution = restitution;
    }

    public double getPenetrationCost() {
        return penetrationCost;
    }

    public double getRestitution() {
        return restitution;
    }
}
