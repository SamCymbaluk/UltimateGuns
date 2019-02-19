package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.grenades.Grenade;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag extends Grenade {

    public Frag(Player thrower) {
        super(thrower, 25, Material.COAL);
    }

    @Override
    public boolean onImpact(Vector path, Location loc) {
        return false;
    }

    @Override
    public void onTick(Location loc, int tick) {
        if (tick >= 80) { //Explode
            explodeEffect(loc.getBlock().getLocation().add(0.5, 0.5, 0.5));
            explode(loc);
            getProjectile().end();
        }
    }

    public void explode(Location loc) {
        Location center = loc.getBlock().getLocation().add(0.5, 0.5, 0.5);
        Map<Block, ArrayList<LivingEntity>> targets = new HashMap<>();

        loc.getWorld().getNearbyEntities(loc, 11, 11, 11).stream().filter(LivingEntity.class::isInstance).forEach(ent -> {
            Block b = ent.getLocation().getBlock();
            if (targets.containsKey(b)) {
                ArrayList<LivingEntity> ents = targets.get(b);
                ents.add(((LivingEntity) ent));
                targets.replace(b, ents);
            } else {
                ArrayList<LivingEntity> ents = new ArrayList<>();
                ents.add((LivingEntity) ent);
                targets.put(ent.getLocation().getBlock(), ents);
            }
        });

        List<LivingEntity> hitTargets = new ArrayList<>();

        Vector start = center.toVector();
        List<Vector> vectors = getExplosionVectors(start.clone(), 10, 10, 10, false);
        for (Vector path : vectors) {
            BlockIterator iterator = new BlockIterator(loc.getWorld(), start, path, 0, (int) Math.ceil(path.length()));
            while (iterator.hasNext()) {
                Block b = iterator.next();
                if (targets.containsKey(b)) {
                    hitTargets.addAll(targets.get(b));
                    targets.remove(b);
                }
                if (b.getType().isSolid()) break;
            }
        }

        for (LivingEntity ent : hitTargets) {
            ent.damage(30 - (2.5 * center.distance(ent.getLocation())), getThrower());
            ent.setVelocity(ent.getLocation().toVector().subtract(start.clone()).normalize());
            if (ent instanceof Player) {
                Player player = (Player) ent;
                player.playSound(player.getEyeLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 10));
            }
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
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0, 0, 0, 0.1, null, true);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 300, 0.5, 0.5, 0.5, 0.5, null, true);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 100, 0.5, 0.5, 0.5, 1.0, null, true);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 6, 0.75F);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 6, 1);
    }
}
