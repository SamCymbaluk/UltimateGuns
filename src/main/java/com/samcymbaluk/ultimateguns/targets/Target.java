package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, Set<Entity> ignored) {
        // TODO custom target logic
        return start.getWorld().rayTrace(start, direction, maxDistance, FluidCollisionMode.ALWAYS, true, 0, entity -> !ignored.contains(entity));
    }

    public static Target fromRayTrace(RayTraceResult rtResult) {
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
