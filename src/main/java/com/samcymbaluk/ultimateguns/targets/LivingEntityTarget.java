package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class LivingEntityTarget extends Target {

    private final LivingEntity entity;

    public LivingEntityTarget(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean onHit(Entity ent, double damage) {
        entity.damage(damage, ent);
        entity.setNoDamageTicks(0);

        return entity.isDead();
    }

    @Override
    public RayTraceResult isHit(Location start, Vector direction, double maxDistance) {
        if (start.getWorld().equals(entity.getWorld())) {
            return entity.getBoundingBox().rayTrace(start.toVector(), direction, maxDistance);
        }
        return null;
    }

    @Override
    public int getPenetrationCost() {
        return 10;
    }

    @Override
    public double getRestitution() {
        return 0.1;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LivingEntityTarget) {
            return ((LivingEntityTarget) obj).getEntity().equals(entity);
        }
        return false;
    }
}