package com.samcymbaluk.ultimateguns.environment;

import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;

public class EntityProperties {

    private Double penetrationCost;
    private Double restitution;

    private ConfigParticle gibsParticle;

    public EntityProperties(Double penetrationCost, Double restitution, ConfigParticle gibsParticle) {
        this.penetrationCost = penetrationCost;
        this.restitution = restitution;
        this.gibsParticle = gibsParticle;
    }

    public Double getPenetrationCost() {
        return penetrationCost;
    }

    public Double getRestitution() {
        return restitution;
    }

    public ConfigParticle getGibsParticle() {
        return gibsParticle;
    }
}
