package com.samcymbaluk.ultimateguns.targets;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BlockTarget extends Target {

    private Block block;

    public BlockTarget(Block block) {
        this.block = block;
    }

    @Override
    public boolean onHit(Entity ent, double damage) {
        return false;
    }

    @Override
    public RayTraceResult isHit(Location start, Vector direction, double maxDistance) {
        return block.rayTrace(start, direction, maxDistance, FluidCollisionMode.ALWAYS);
    }

    @Override
    public double getPenetrationCost() {
        if (block.isPassable() && !block.isLiquid()) {
            return 0;
        } else {
            return UltimateGuns.getInstance().getEnvironmentConfig().getPenetrationCost(block.getType());
        }
    }

    @Override
    public double getRestitution() {
        return UltimateGuns.getInstance().getEnvironmentConfig().getRestitution(block.getType());
    }

    @Override
    public Location getLocation() {
        return block.getLocation();
    }

    public Block getBlock() {
        return block;
    }

    public boolean isDestructible() {
        return UltimateGuns.getInstance().getEnvironmentConfig().isDestructible(block.getType());
    }

    public double getDestructionThreshold() {
        return UltimateGuns.getInstance().getEnvironmentConfig().getDestructionThreshold(block.getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockTarget) {
            return ((BlockTarget) obj).getBlock().equals(block);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return block.hashCode();
    }
}