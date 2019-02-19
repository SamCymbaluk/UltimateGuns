package com.samcymbaluk.ultimateguns.targets;

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
        return block.rayTrace(start, direction, maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public int getPenetrationCost() {
        switch (block.getType()) {
            /*
            case LOG:
                return 25;
            case WATER:
            case STATIONARY_WATER:
                return 10;
            case LEAVES:
                return 1;
            case GLASS:
            case STAINED_GLASS:
            case STAINED_GLASS_PANE:
            case THIN_GLASS:
                return 1;
            case WOODEN_DOOR:
                return 15;
            case WOOD:
                return 20;
            case FENCE:
            case JUNGLE_FENCE:
                return 10;
            case SANDSTONE:
            case SANDSTONE_STAIRS:
                return 30;
            case STONE:
                return 45;
            case COBBLESTONE:
            case COBBLESTONE_STAIRS:
            case MOSSY_COBBLESTONE:
            case SMOOTH_BRICK:
            case SMOOTH_STAIRS:
                return 45;
            case QUARTZ_STAIRS:
                return 5;
            case ACACIA_STAIRS:
                return 10;
            case NETHER_BRICK:
            case NETHER_BRICK_STAIRS:
                return 30;
            case QUARTZ_BLOCK:
                return 40;
            case STAINED_CLAY:
                return 30;
            case DIAMOND_ORE:
            case BRICK:
                return 75;
            case WOOL:
                return 20;
                */
            default:
                return -1;
        }
    }

    @Override
    public double getRestitution() {
        return 0.4;
    }

    @Override
    public Location getLocation() {
        return block.getLocation();
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockTarget) {
            return ((BlockTarget) obj).getBlock().equals(block);
        }
        return false;
    }


}