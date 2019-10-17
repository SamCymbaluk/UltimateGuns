package com.samcymbaluk.ultimateguns;

import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.RayTraceTargetResult;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class UltimateGunsProjectile {

    private final LivingEntity owner;
    private final boolean ignoreOwner;
    private final double initialVelocity;
    private final double dragCoefficient;
    private final double gravity;
    private final double maxDistance;

    private Location loc;
    private ProjectileCallback callback;
    private boolean killed = false;

    /**
     * @param owner
     * @param ignoreOwner
     * @param initialVelocity Initial velocity in metres per tick
     * @param dragCoefficient
     * @param gravity
     * @param maxDistance
     */
    public UltimateGunsProjectile(LivingEntity owner, boolean ignoreOwner, double initialVelocity, double dragCoefficient, double gravity, double maxDistance) {
        this.owner = owner;
        this.ignoreOwner = ignoreOwner;
        this.initialVelocity = initialVelocity;
        this.dragCoefficient = dragCoefficient;
        this.gravity = gravity;
        this.maxDistance = maxDistance;
    }

    public void start(Location loc, Vector direction, ProjectileCallback callback) {
        this.loc = loc;
        this.callback = callback;
        Set<Target> ignored = new HashSet<>();
        if (ignoreOwner) ignored.add(new LivingEntityTarget(owner));

        step(loc.clone(), direction.clone().normalize().multiply(initialVelocity), 0, ignored);

    }

    private void step(Location start, Vector path, double totalDistance, Set<Target> ignored) {
        double distance = path.length();
        if (totalDistance + distance > maxDistance) {
            distance = maxDistance - totalDistance;
        }
        path.normalize().multiply(distance);

        // Distance termination
        if (distance <= 1e-3) {
            callback.done(start);
            return;
        }

        RayTraceTargetResult rtResult;
        double tickElapsed = 0;
        double pathLength = path.length();
        // Loop while there is a target and there is still (simulated) tick time left
        while ((rtResult = Target.rayTrace(start, path, pathLength * (1 - tickElapsed), ignored::contains)) != null && tickElapsed < 1) {
            Target target = rtResult.getTarget();
            Vector newPath = callback.handleImpact(rtResult, path.clone(), totalDistance + rtResult.getRayTraceResult().getHitPosition().distance(start.toVector()), pathLength);
            Location newStart = rtResult.getRayTraceResult().getHitPosition().toLocation(loc.getWorld());

            callback.handleStep(start, newStart.clone().subtract(start).toVector(), pathLength);

            // Killed short circuit
            if (killed) return;

            // t = d/v
            tickElapsed += newStart.toVector().subtract(start.toVector()).length() / pathLength;

            // Update iteration vars
            ignored.add(target);
            path = newPath;
            pathLength = path.length();
            start = newStart;
        }

        // No collisions
        if (tickElapsed == 0) {
            callback.handleStep(start, path, path.length());
            start.add(path);
        }

        // Gravity
        path.add(new Vector(0, -gravity, 0));

        // Velocity drop off
        // V = V * ((V - dV) / V)
        // where dV = ((C/m)v^2*dt)
        //       C/m is dragCoefficient
        path.multiply(1.0 - dragCoefficient * path.length());

        // New step variables
        Location newStart = start;
        Vector newPath = path;
        double newTotalDistance = totalDistance + distance;
        Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> {
            if (!killed) step(newStart, newPath, newTotalDistance, ignored);
        }, 1);
    }

    public void kill() {
        this.killed = true;
    }

    public void debugProjectileEffect(Vector start, Vector end) {
        Vector path = end.clone().subtract(start);
        for (double i = 0; i < path.length(); i += 2.5) {

            Vector pos = start.clone().add(path.clone().normalize().multiply(i));
            for (Player p : loc.getWorld().getPlayers()) {
                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, pos.toLocation(p.getWorld()), 1, 0, 0, 0, 0, null, true);
            }

        }
    }
}
