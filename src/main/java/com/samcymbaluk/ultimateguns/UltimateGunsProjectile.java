package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collections;

public class UltimateGunsProjectile {

    private final LivingEntity owner;
    private final boolean ignoreOwner;
    private final double initialVelocity;
    private final double gravity;
    private final double maxDistance;

    private Location loc;
    private ProjectileCallback callback;
    private double totalDistance = 0;

    /**
     * @param owner
     * @param ignoreOwner
     * @param initialVelocity Initial velocity in metres per tick
     * @param gravity
     * @param maxDistance
     */
    public UltimateGunsProjectile(LivingEntity owner, boolean ignoreOwner, double initialVelocity, double gravity, double maxDistance) {
        this.owner = owner;
        this.ignoreOwner = ignoreOwner;
        this.initialVelocity = initialVelocity;
        this.gravity = gravity;
        this.maxDistance = maxDistance;
    }

    public void start(Location loc, Vector direction, ProjectileCallback callback) {
        this.loc = loc;
        this.callback = callback;

        step(loc.toVector(), direction.normalize(), initialVelocity, initialVelocity, ignoreOwner ? new LivingEntityTarget(owner) : null);
    }


    /**
     *
     * @param start Where the project begins the step from
     * @param path The normalized direction
     * @param velocity The velocity of the projectile in metres per tick
     * @param distLeft The distance left to travel this tick
     * @param ignoredTarget A target that should be ignored (or null)
     */
    private void step(Vector start, Vector path, double velocity, double distLeft, Target ignoredTarget) {
        // Max distance check

        System.out.println("Distance: " + totalDistance);
        System.out.println("Velocity: " + velocity);
        if (totalDistance + distLeft > maxDistance) {
            distLeft = maxDistance - totalDistance;
        }

        // Distance left stoppage
        if (distLeft <= 0) {
            callback.done(start.toLocation(loc.getWorld()));
            return;
        }

        // Velocity stoppage
        if (velocity < 0.05) {
            callback.done(start.toLocation(loc.getWorld()));
            return;
        }

        RayTraceResult rtResult = Target.rayTrace(start.toLocation(loc.getWorld()), path, velocity, Collections.singleton(ignoredTarget));
        Target target = Target.fromRayTrace(rtResult);

        Vector newPath = path.clone();
        Vector newStart = start.clone().add(path.clone().multiply(distLeft));

        // Handle collision if it occurred
        if (rtResult != null) {

            newPath = callback.handleImpact(rtResult, target, path.clone().multiply(velocity));
            velocity = newPath.length();
            newPath.normalize();

            newStart = rtResult.getHitPosition();
        }

        // Calculate distance travelled this step
        double distanceTravelled = start.clone().subtract(newStart).length();

        System.out.println(distanceTravelled);

        // Gravity
        newPath.add(new Vector(0, -gravity*(distanceTravelled/velocity), 0));

        // Update distance travelled
        totalDistance += distanceTravelled;

        // Call next iteration of step
        callback.handleStep(start.toLocation(loc.getWorld()), newStart.clone().subtract(start));
        final Vector nextStart = newStart;
        final Vector nextPath = newPath;
        final double vel = velocity;
        if (distanceTravelled < distLeft - 0.01) {
            // More to do in same tick
            step(nextStart, nextPath, vel, distLeft - distanceTravelled, target);
        } else {
            // New tick -> Reset distLeft
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> step(nextStart, nextPath, vel, vel, target), 1);
        }
    }

    public void debugProjectileEffect(Vector start, Vector end) {
        Vector path = end.clone().subtract(start);
        for (double i = 0; i < path.length(); i += 5) {

            Vector pos = start.clone().add(path.clone().normalize().multiply(i));
            for (Player p : loc.getWorld().getPlayers()) {
                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, pos.toLocation(p.getWorld()), 1, 0, 0, 0, 0, null, true);
            }
            break;

        }
    }
}
