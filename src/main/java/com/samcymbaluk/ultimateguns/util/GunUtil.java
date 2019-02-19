package com.samcymbaluk.ultimateguns.util;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class GunUtil {

    /*
    public static double getHitDistance(Vector start, Vector pathfrac, BoundingBox aabb) {
        double t = -1;
        aabb.
        Vector lb = new Vector(aabb.a, aabb.b, aabb.c);
        Vector rt = new Vector(aabb.d, aabb.e, aabb.f);

        // lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
        // start is origin of ray
        double t1 = (lb.getX() - start.getX()) * pathfrac.getX();
        double t2 = (rt.getX() - start.getX()) * pathfrac.getX();
        double t3 = (lb.getY() - start.getY()) * pathfrac.getY();
        double t4 = (rt.getY() - start.getY()) * pathfrac.getY();
        double t5 = (lb.getZ() - start.getZ()) * pathfrac.getZ();
        double t6 = (rt.getZ() - start.getZ()) * pathfrac.getZ();

        double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but whole AABB is behind us
        if (tmax < 0) {
            t = tmax;
            return -1;
        }

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {
            t = tmax;
            return -1;
        }

        t = tmin;
        return t;
    }

    public static BlockTarget getEndBlock(World world, Vector start, Vector path, int distance, Vector pathfrac, Target ignoredTarget) {
        return getEndBlock(world, start, path, distance, pathfrac, ignoredTarget, true);
    }

    public static BlockTarget getEndBlock(World world, Vector start, Vector path, int distance, Vector pathfrac, Target ignoredTarget, boolean includeLiquid) {
        if (path.length() < 0.0001) return null;
        BlockIterator iterator = new BlockIterator(world, start, path, 0, distance);
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            Block b = iterator.next();

            if (b.getType().isSolid() || (includeLiquid && b.isLiquid())) {
                BlockTarget blockTarget = new BlockTarget(b);
                if (blockTarget.isHit(start.clone(), path.clone(), pathfrac.clone()) != null && !blockTarget.equals(ignoredTarget)) {
                    return blockTarget;
                }
            }
            if (i > 5000) return null;
        }
        return null;
    }

    public static void playRealisticSound(Location location, String sound, float volume, float pitch, double distantThreshold, String distanceModifier) {
        final Location loc = location.clone();

        for (Player obs : loc.getWorld().getPlayers()) {
            double distance = obs.getLocation().distance(loc);
            if (distance <= volume * 16) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Crisis.getInstance(), () -> {
                    if (obs.getWorld().equals(loc.getWorld())) {
                        obs.playSound(location, sound + (distance < distantThreshold ? "" : distanceModifier), volume, pitch);
                    }
                }, (int) (distance / 16D));
            }
        }
    }

    public static void playRealisticSound(Location location, String sound, float volume, float pitch) {
        playRealisticSound(location, sound, volume, pitch, 0, "");
    }

    public static void playRealisticSound(Location location, Sound sound, float volume, float pitch) {
        playRealisticSound(location, "minecraft:" + sound.toString().replace("_", "."), volume, pitch, 0, "");
    }
    */
}
