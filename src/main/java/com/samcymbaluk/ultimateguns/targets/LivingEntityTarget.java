package com.samcymbaluk.ultimateguns.targets;

import com.samcymbaluk.ultimateguns.UltimateGuns;
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
    public Vector onHit(Entity ent, double damage, RayTraceTargetResult impact, Vector path, double distance, double velocity) {
        entity.damage(damage, ent);
        entity.setNoDamageTicks(0);

        return path;
    }

    @Override
    public RayTraceTargetResult isHit(Location start, Vector direction, double maxDistance) {
        if (start.getWorld().equals(entity.getWorld())) {
            return new RayTraceTargetResult(entity.getBoundingBox().rayTrace(start.toVector(), direction, maxDistance), this);
        }
        return null;
    }

    @Override
    public double getPenetrationCost() {
        return UltimateGuns.getInstance().getEnvironmentConfig().getPenetrationCost(entity.getType());
    }

    @Override
    public double getRestitution() {
        return UltimateGuns.getInstance().getEnvironmentConfig().getRestitution(entity.getType());
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

    @Override
    public int hashCode() {
        return entity.hashCode();
    }
}