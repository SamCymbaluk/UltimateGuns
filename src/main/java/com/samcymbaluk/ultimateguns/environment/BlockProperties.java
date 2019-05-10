package com.samcymbaluk.ultimateguns.environment;

import jdk.internal.jline.internal.Nullable;

public class BlockProperties {

    private Double penetrationCost;
    private Double restitution;

    private Boolean destructible;
    private Double destructionThreshold;

    public BlockProperties(Double penetrationCost, Double restitution, Boolean destructible, Double destructionThreshold) {
        this.penetrationCost = penetrationCost;
        this.restitution = restitution;
        this.destructible = destructible;
        this.destructionThreshold = destructionThreshold;
    }

    public Double getPenetrationCost() {
        return penetrationCost;
    }

    public Double getRestitution() {
        return restitution;
    }

    @Nullable
    public Boolean isDestructible() {
        return destructible;
    }

    public Double getDestructionThreshold() {
        return destructionThreshold;
    }
}
