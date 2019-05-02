package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.UltimateGunsProjectile;
import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import com.samcymbaluk.ultimateguns.grenades.Grenade;
import com.samcymbaluk.ultimateguns.targets.BlockTarget;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragGrenade extends Grenade {

    FragFeature fragFeature;
    FragFeatureConfig conf;

    public FragGrenade(FragFeature fragFeature, Player thrower) {
        super(thrower, fragFeature.getConfig().getThrowVelocity(), fragFeature.getConfig().getEntityMaterial(), fragFeature.getConfig());
        this.fragFeature = fragFeature;
        this.conf = fragFeature.getConfig();
    }

    @Override
    public boolean onImpact(Vector path, Location loc) {
        return false;
    }

    @Override
    public void onTick(Location loc, int tick) {
        if (tick >= conf.getFuse()) { //Explode
            explodeEffect(loc.getBlock().getLocation().add(0.5, 0.5, 0.5));
            explode(loc);
            getProjectile().end();
        }
    }

    public void explode(Location loc) {
        Location center = loc.getBlock().getLocation().add(0.5, 0.5, 0.5);
        Set<Target> hitTargets = new HashSet<>();
        Vector start = center.toVector();

        double radius = conf.getExplosionRadius();
        List<Vector> vectors = getExplosionVectors(start.clone(), radius, radius, radius, false);
        for (Vector vector : vectors) {
            UltimateGunsProjectile proj = new UltimateGunsProjectile(getThrower(), true, 50, 0, radius + 5);
            proj.start(center, vector, new ProjectileCallback() {
                @Override
                public Vector handleImpact(RayTraceResult impact, Target target, Vector path) {
                    if (!hitTargets.contains(target)) {
                        target.onHit(getThrower(),
                                conf.getInitialDamage()
                                - (conf.getDamageDropoff() * center.distance(target.getLocation())));
                        hitTargets.add(target);
                    }
                    if (target instanceof BlockTarget) {
                        BlockTarget bt = (BlockTarget) target;

                        if (!bt.getBlock().isPassable()) proj.kill();
                    }

                    return path;
                }

                @Override
                public void handleStep(Location start, Vector path, double velocity) {
                }

                @Override
                public void done(Location end) {
                }
            });
        }
    }

    public List<Vector> getExplosionVectors(Vector pos, double radiusX, double radiusY, double radiusZ, boolean filled) {
        List<Vector> vectors = new ArrayList<>();

        radiusX += 0.5;
        radiusY += 0.5;
        radiusZ += 0.5;

        final double invRadiusX = 1 / radiusX;
        final double invRadiusY = 1 / radiusY;
        final double invRadiusZ = 1 / radiusZ;

        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusY = (int) Math.ceil(radiusY);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);

        double nextXn = 0;
        forX:
        for (double x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY:
            for (double y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ:
                for (double z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    if (!filled) {
                        if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }

                    vectors.add(pos.clone().add(new Vector(x, y, z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(-x, y, z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(x, -y, z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(x, y, -z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(-x, -y, z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(x, -y, -z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(-x, y, -z).subtract(pos)));
                    vectors.add(pos.clone().add(new Vector(-x, -y, -z).subtract(pos)));

                }
            }
        }

        return vectors;
    }

    private static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    private void explodeEffect(Location loc) {
        ConfigParticle.spawnAll(conf.getExplosionParticles(), loc);
        ConfigSound.playAll(conf.getExplosionSounds(), loc, getThrower());
    }
}
