package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Target {

    private static Set<Target> customTargets = new HashSet<>();

    public static void registerCustomTarget(Target target) {
        customTargets.add(target);
    }

    public static void removeCustomTarget(Target target) {
        customTargets.remove(target);
    }

    public static RayTraceTargetResult rayTrace(Location start, Vector direction, double maxDistance) {
        return rayTrace(start, direction, maxDistance, null);
    }

    /**
     *
     * @param start
     * @param direction
     * @param maxDistance
     * @param ignorePredicate Targets that meet this predicate will not be considered
     * @return
     */
    public static RayTraceTargetResult rayTrace(Location start, Vector direction, double maxDistance, Predicate<Target> ignorePredicate) {
        if (ignorePredicate == null) ignorePredicate = Predicate.isEqual(null);

        if (direction.lengthSquared() < 1e-5 || maxDistance <= 1e-5) return null;

        // TODO IllegalStateException occurs when (e.g.) close to edge of block (e.g. -662.9999999999998,61.99999999999981,-1220.0)
        double distFromBlock = start.getBlock().getLocation().distanceSquared(start);
        if (Math.abs(Math.round(distFromBlock) - distFromBlock) <= 1e-5) {
            start.add(0.001, 0.001, 0.001);
        }

        RayTraceTargetResult blockRayTrace = rayTraceBlocks(start, direction, maxDistance, ignorePredicate);
        RayTraceTargetResult entityRayTrace = rayTraceEntities(start, direction, maxDistance, ignorePredicate);
        RayTraceTargetResult customRayTrace = rayTraceCustomTargets(start, direction, maxDistance, ignorePredicate);

        return min(start, blockRayTrace, entityRayTrace, customRayTrace);
    }

    public static RayTraceTargetResult rayTraceBlocks(Location start, Vector direction, double maxDistance, Predicate<Target> ignorePredicate) {
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
            if ((!ignorePredicate.test(new BlockTarget(block))) && !block.isEmpty()) {

                // Secondary rough check with the simplified block geometry
                RayTraceResult res = (block.isLiquid() ? BoundingBox.of(block) : block.getBoundingBox()).rayTrace(start.toVector(), direction, maxDistance);
                if (res != null) {
                    // Now check if the collision still occurs with the precise collision geometry of the block
                    // TODO Block#rayTrace broken (https://hub.spigotmc.org/jira/browse/SPIGOT-5370)
                    // This is why we must only perform the Block#rayTrace with a start location already within the block
                    Location newStart = res.getHitPosition().toLocation(start.getWorld());
                    double newMaxDistance = maxDistance - start.distance(newStart);
                    res = block.rayTrace(newStart, direction, newMaxDistance, FluidCollisionMode.ALWAYS);

                    // If Block#rayTrace worked correctly, block == res.getHitBlock() should always be true
                    if (res != null && block.equals(res.getHitBlock())) {
                        return new RayTraceTargetResult(res, new BlockTarget(block));
                    }
                }

            }

        }

        return null;
    }

    public static RayTraceTargetResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Target> ignorePredicate) {
        RayTraceResult entityRT = start.getWorld().rayTraceEntities(
                start,
                direction,
                maxDistance,
                entity -> entity instanceof LivingEntity && (!ignorePredicate.test(new LivingEntityTarget((LivingEntity) entity)))
        );
        return entityRT == null ? null : new RayTraceTargetResult(entityRT, new LivingEntityTarget((LivingEntity) entityRT.getHitEntity()));
    }

    public static RayTraceTargetResult rayTraceCustomTargets(Location start, Vector direction, double maxDistance, Predicate<Target> ignorePredicate) {
        return min(start, customTargets.stream()
                .filter(ignorePredicate.negate())
                .map(target -> target.isHit(start, direction, maxDistance))
                .collect(Collectors.toList()));
    }

    private static RayTraceTargetResult min(Location start, RayTraceTargetResult... results) {
        return min(start, Arrays.asList(results));
    }

    private static RayTraceTargetResult min(Location start, Collection<RayTraceTargetResult> results) {
        return results.stream().reduce(null, (r1, r2) -> min(start, r1, r2));
    }

    /**
     * Returns the smaller of the two RayTraceTargetResults
     * Only returns null if both are null
     * @param start Start location
     * @param r1 First RayTraceTargetResult
     * @param r2 Second RayTraceTargetResult
     * @return The RayTraceTargetResult closest to the start Location
     */
    private static RayTraceTargetResult min(Location start, RayTraceTargetResult r1, RayTraceTargetResult r2) {
        if (r1 != null && r2 != null) {
            // Return closer collision
            double r1Dist = r1.getRayTraceResult().getHitPosition().distanceSquared(start.toVector());
            double r2Dist = r2.getRayTraceResult().getHitPosition().distanceSquared(start.toVector());
            return r1Dist < r2Dist ? r1 : r2;
        } else if (r1 != null) {
            return r1;
        } else if (r2 != null){
            return r2;
        } else {
            return null;
        }
    }

    private static Target fromRayTrace(RayTraceResult rtResult) {
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
     * @param impact The impact information
     * @param path The direction and magnitude (velocity) of the projectile when the impact occurred
     * @param distance The total distance travelled by the projectile up until the impact
     * @param velocity The velocity of the projectile at the time of impact
     * @return The resulting path
     */
    public abstract Vector onHit(Entity ent, double damage, RayTraceTargetResult impact, Vector path, double distance, double velocity);

    /**
     * @param start
     * @param direction
     * @param maxDistance
     * @return Returns where the given ray hits the target, null if it does not
     */
    public abstract RayTraceTargetResult isHit(Location start, Vector direction, double maxDistance);

    public abstract Location getLocation();

    /**
     * @return The penetration cost
     */
    public abstract double getPenetrationCost();

    /**
     * @return The collision restitution
     */
    public abstract double getRestitution();
}
