package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collections;

public class UltimateGunsProjectile {

    private final Entity owner;
    private final double velocity;
    private final double gravity;

    private Location loc;
    private ProjectileCallback callback;

    public UltimateGunsProjectile(Entity owner, double velocity, double gravity) {
        this.owner = owner;
        this.velocity = velocity;
        this.gravity = gravity;
    }

    public void start(Location loc, Vector direction, ProjectileCallback callback) {
        this.loc = loc;
        this.callback = callback;
    }


    /**
     *
     * @param start Where the project begins the step from
     * @param path Should have a length equal to the velocity (units m / (s * 20))
     * @param ignoredTarget When the previous step ends at a target, passing it to the next target ensures it is ignored
     */
    private void step(Vector start, Vector path, Target ignoredTarget) {
        RayTraceResult rtResult = Target.rayTrace(start.toLocation(loc.getWorld()), path, path.length(), Collections.singleton(ignoredTarget));

        Vector newPath = path.clone();
        Vector newStart = start.clone().add(path);

        // Handle collision if it occurred
        if (rtResult != null) {
            newPath = callback.handleImpact(rtResult, path.clone());
            if (newPath.lengthSquared() == 0) return;

            newStart = rtResult.getHitPosition();
        }

        // Gravity
        path.add(new Vector(0, -gravity, 0));

        // Call next iteration of step
        final Vector nextStart = newStart;
        final Vector nextPath = newPath;
        Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> step(nextStart, nextPath, Target.fromRayTrace(rtResult)));
    }
}
