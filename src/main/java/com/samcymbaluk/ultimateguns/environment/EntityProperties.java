package com.samcymbaluk.ultimateguns.environment;

public class EntityProperties {

    private Double penetrationCost;
    private Double restitution;

    public EntityProperties(Double penetrationCost, Double restitution) {
        this.penetrationCost = penetrationCost;
        this.restitution = restitution;
    }

    public Double getPenetrationCost() {
        return penetrationCost;
    }

    public Double getRestitution() {
        return restitution;
    }
}
