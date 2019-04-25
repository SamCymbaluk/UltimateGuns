package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public abstract class Target {

    private static List<Target> customTargets = new ArrayList<>();

    public static void registerCustomTarget(Target target) {
        customTargets.add(target);
    }

    public static void removeCustomTarget(Target target) {
        customTargets.remove(target);
    }

    public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance) {
        // TODO custom target logic
        return rayTrace(start, direction, maxDistance, null);
    }

    /**
     *
     * @param start
     * @param direction
     * @param maxDistance
     * @param ignored Targets that meet this predicate will not be considered
     * @return
     */
    public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, Predicate<Target> ignored) {
        // TODO custom target logic

        if (direction.lengthSquared() < 1e-5 || maxDistance <= 1e-5) return null;

        // TODO IllegalStateException occurs when (e.g.) close to edge of block (e.g. -662.9999999999998,61.99999999999981,-1220.0)
        double distFromBlock = start.getBlock().getLocation().distanceSquared(start);
        if (Math.abs(Math.round(distFromBlock) - distFromBlock) <= 1e-5) {
            start.add(0.001, 0.001, 0.001);
        }

        // Look for block collisions
        RayTraceResult blockRayTrace = null;

        // TODO further investigation into IllegalStateExceptions in BlockIterator
        BlockIterator bIterator;
        try {
            bIterator = new BlockIterator(start.getWorld(), start.toVector(), direction, 0, (int) Math.ceil(maxDistance));
        } catch (IllegalStateException ex) {
            System.out.println("Location: " + start.toVector());
            System.out.println("Direction: " + direction);
            System.out.println("Max Distance: " + maxDistance);
            System.out.println("From block: " + start.getBlock().getLocation().distanceSquared(start));
            return null;
        }

        while(bIterator.hasNext()) {

            Block block = bIterator.next();
            // First perform a rough collision check with BlockIterator
            if ((ignored == null || !ignored.test(new BlockTarget(block))) && !block.isEmpty()) {

                // Now check if the collision still occurs with the precise collision geometry of the block
                RayTraceResult res = block.rayTrace(start, direction, maxDistance, FluidCollisionMode.ALWAYS);
                if (res != null) {
                    blockRayTrace = res;
                    break;
                }

            }

        }

        // Look for entity collisions
        RayTraceResult entityRayTrace = start.getWorld().rayTraceEntities(
                start,
                direction,
                maxDistance,
                entity -> entity instanceof LivingEntity && (ignored == null || !ignored.test(new LivingEntityTarget((LivingEntity) entity)))
        );

        // Return closest RayTraceResult
        return min(start, blockRayTrace, entityRayTrace);
    }

    /**
     * Returns the smaller of the two RayTraceResults
     * Only returns null if both are null
     * @param start Start location
     * @param r1 First RayTraceResult
     * @param r2 Second RayTraceResult
     * @return The RayTraceResult closest to the start Location
     */
    private static RayTraceResult min(Location start, RayTraceResult r1, RayTraceResult r2) {
        if (r1 != null && r2 != null) {
            // Return closer collision
            return r1.getHitPosition().distanceSquared(start.toVector()) < r2.getHitPosition().distanceSquared(start.toVector()) ? r1 : r2;
        } else if (r1 != null) {
            return r1;
        } else if (r2 != null){
            return r2;
        } else {
            return null;
        }
    }

    public static Target fromRayTrace(RayTraceResult rtResult) {
        // TODO custom target logic
        if (rtResult != null) {
            if (rtResult.getHitEntity() != null && rtResult.getHitEntity() instanceof LivingEntity) {
                return new LivingEntityTarget((LivingEntity) rtResult.getHitEntity());
            }
            if (rtResult.getHitBlock() != null) {
                return new BlockTarget(rtResult.getHitBlock());
            }
        }

        return null;
    }

    /**
     * @param ent The entity which fired the gun
     * @param damage The suggested damage to apply
     * @return Should return true if the CustomTarget has been destroyed
     */
    public abstract boolean onHit(Entity ent, double damage);

    /**
     * @param start
     * @param direction
     * @param maxDistance
     * @return Returns where the given ray hits the target, null if it does not
     */
    public abstract RayTraceResult isHit(Location start, Vector direction, double maxDistance);

    public abstract Location getLocation();

    /**
     * @return The penetration cost
     */
    public abstract int getPenetrationCost();

    /**
     * @return The collision restitution
     */
    public abstract double getRestitution();
}
