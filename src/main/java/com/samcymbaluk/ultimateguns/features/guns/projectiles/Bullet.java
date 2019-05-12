package com.samcymbaluk.ultimateguns.features.guns.projectiles;

import com.samcymbaluk.ultimateguns.UltimateGunsProjectile;
import com.samcymbaluk.ultimateguns.features.guns.Gun;
import com.samcymbaluk.ultimateguns.features.guns.GunFeature;
import com.samcymbaluk.ultimateguns.targets.BlockTarget;
import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Bullet extends GunProjectile implements ProjectileCallback {

    private UltimateGunsProjectile proj;
    private double penLeft;

    public Bullet(Gun gun, LivingEntity owner) {
        super(gun, owner);
    }

    @Override
    public void fire() {
        proj = new UltimateGunsProjectile(
                getOwner(),
                true,
                getGun().getCaliber().getMuzzleVelocity() / 20.0,
                GunFeature.getInstance().getConfig().getGravity(),
                255
                );

        penLeft = getGun().getCaliber().getPenetration();
        proj.start(getOwner().getEyeLocation().subtract(0, 0.25, 0), getOwner().getLocation().getDirection(), this);
    }

    @Override
    public Vector handleImpact(RayTraceResult impact, Target target, Vector path) {
        if (target instanceof LivingEntityTarget) {
            target.onHit(getOwner(), 100);
        }

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

        penLeft -= penLost;
        if (penLeft <= 0) proj.kill();
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
