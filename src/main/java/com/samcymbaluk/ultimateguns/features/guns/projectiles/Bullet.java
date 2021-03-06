package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.UltimateGunsProjectile;
import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import com.samcymbaluk.ultimateguns.config.util.ConfigPotionEffect;
import com.samcymbaluk.ultimateguns.features.guns.Gun;
import com.samcymbaluk.ultimateguns.features.guns.GunCaliber;
import com.samcymbaluk.ultimateguns.features.guns.GunFeature;
import com.samcymbaluk.ultimateguns.targets.BlockTarget;
import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.RayTraceTargetResult;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.NmsUtil;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;


public class Bullet extends GunProjectile implements ProjectileCallback {

    private UltimateGunsProjectile proj;
    private double penLeft;

    private ThreadLocalRandom random;

    public Bullet(Gun gun, GunCaliber caliber, Player owner) {
        super(gun, caliber, owner);
        random = ThreadLocalRandom.current();
    }

    @Override
    public void fire() {
        proj = new UltimateGunsProjectile(
                getOwner(),
                true,
                getCaliber().getMuzzleVelocity() / 20.0,
                getCaliber().getDragCoefficient(),
                GunFeature.getInstance().getConfig().getGravity(),
                255);

        penLeft = getCaliber().getPenetration();
        Location start = getOwner().getEyeLocation().subtract(0, 0.25, 0);
        Vector path = getOwner().getLocation().getDirection();
        getGun().applyRecoil(path);
        getGun().applyAccuracy(path, UltimateGuns.getInstance().getGunPlayer(getOwner()));

        proj.start(start, path, this);
        shotEffect(start, path);
    }

    @Override
    public Vector handleImpact(RayTraceTargetResult impact, Vector path, double distance, double velocity) {
        Target target = impact.getTarget();
        double damage = getCaliber().getDamage() - (distance * getCaliber().getDamageDropoff());
        damage *= velocity / (getCaliber().getMuzzleVelocity() / 20.0);

        if (target instanceof LivingEntityTarget) {
            LivingEntityTarget leTarget = (LivingEntityTarget) target;
            if (leTarget.getEntity() instanceof Player) {
                boolean headshot = impact.getRayTraceResult().getHitPosition().distanceSquared(leTarget.getEntity().getEyeLocation().toVector()) < 2*0.3*0.3;
                damage = headshot ? damage * getCaliber().getHeadshotMultiplier() : damage;
            }
            hitEffect(impact.getRayTraceResult().getHitPosition().toLocation(target.getLocation().getWorld()), (LivingEntityTarget) target, path);
        } else if (target instanceof BlockTarget) {
            impactEffect(impact.getRayTraceResult().getHitPosition().toLocation(target.getLocation().getWorld()), (BlockTarget) target, path);
        }

        path = target.onHit(getOwner(), damage, impact, path.clone(), distance, velocity);
        handlePenetration(target, path);

        return path;
    }

    private void handlePenetration(Target target, Vector path) {
        double penLost = target.getPenetrationCost();
        double nextPen = penLeft - penLost;

        if (penLost == 0) return;

        if (nextPen > 0) path.multiply((penLeft - penLost) / penLeft);
        Vector deflection = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
        path.add(deflection.normalize().multiply(Math.sqrt(penLost) / 2));

        if (target instanceof BlockTarget) {
            BlockTarget bt = (BlockTarget) target;
            if (penLeft >= bt.getDestructionThreshold() && bt.isDestructible()) {
                BlockBreakEvent event = new BlockBreakEvent(bt.getBlock(), getOwner());
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    bt.getBlock().setType(Material.AIR);
                }
            }
        }

        penLeft -= penLost;
        if (penLeft <= 0) proj.kill();
    }

    private void impactEffect(Location impactLoc, BlockTarget blockTarget, Vector path) {
        double length = getCaliber().getImpactParticleLength();
        int amount = getCaliber().getImpactParticleAmount();
        float spread = getCaliber().getImpactParticleSpread();

        Block endBlock = blockTarget.getBlock();
        if (endBlock.isPassable() && !endBlock.isLiquid()) return;

        Vector v = path.clone().normalize().multiply(-length);
        v.divide(new Vector(amount, amount, amount));
        for (int i = 0; i < amount; i++) {
            impactLoc.add(v);
            impactLoc.getWorld().spawnParticle(Particle.BLOCK_DUST, impactLoc, 1, i * spread, i * spread, i * spread, 0.05F, endBlock.getBlockData(), true);
        }

        float pitch = (float) random.nextDouble(0.7, 1.3);
        if (getCaliber().hasImpactSound()) {
            if (blockTarget.getBlock().getType() == Material.WATER) {
                impactLoc.getWorld().playSound(impactLoc, Sound.ENTITY_BOAT_PADDLE_WATER, 1, pitch);
            } else if (blockTarget.getBlock().getType() == Material.LAVA) {
                impactLoc.getWorld().playSound(impactLoc, Sound.ITEM_BUCKET_FILL_LAVA, 1, pitch);
            } else {
                impactLoc.getWorld().playSound(impactLoc, NmsUtil.blockSound(endBlock), 1, pitch);
            }
        }
    }

    private void hitEffect(Location hitLoc, LivingEntityTarget target, Vector path) {
        double length = getCaliber().getHitParticleLength();
        int amount = getCaliber().getHitParticleAmount();
        float spread = getCaliber().getHitParticleSpread();

        ConfigParticle gibsParticle = UltimateGuns.getInstance().getEnvironmentConfig().getGibsParticle(target.getEntity().getType());

        Vector v = path.clone().normalize().multiply(length);
        v.divide(new Vector(amount, amount, amount));
        for (int i = 0; i < amount; i++) {
            hitLoc.add(v);
            hitLoc.getWorld().spawnParticle(
                    gibsParticle.getParticle(),
                    hitLoc, gibsParticle.getCount(),
                    i * spread,
                    i * spread,
                    i * spread,
                    gibsParticle.getExtra(),
                    gibsParticle.getData(),
                    gibsParticle.getForceDisplay()
            );
        }

        ConfigPotionEffect.addAll(getCaliber().getHitEffects(), target.getEntity(), true);
    }

    @Override
    public void handleStep(Location start, Vector path, double velocity) {
        if (GunFeature.getInstance().getConfig().isDebug()) {
            proj.debugProjectileEffect(start.toVector(), start.clone().add(path).toVector());
        }
    }

    @Override
    public void done(Location end) {

    }
}
