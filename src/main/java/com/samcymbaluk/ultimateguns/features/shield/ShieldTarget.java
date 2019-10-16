package com.samcymbaluk.ultimateguns.features.shield;

import com.samcymbaluk.ultimateguns.targets.BoundingBoxTarget;
import com.samcymbaluk.ultimateguns.targets.RayTraceTargetResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ShieldTarget extends BoundingBoxTarget {

    private Player player;

    public ShieldTarget(Player player) {
        this.player = player;
    }

    @Override
    public RayTraceTargetResult isHit(Location start, Vector direction, double maxDistance) {
        if (/*player.isBlocking() && */player.getInventory().getItemInMainHand().getType() == Material.SHIELD) {
            return super.isHit(start, direction, maxDistance);
        } else {
            return null;
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        Vector eyePos = player.getEyeLocation().toVector();
        Vector eyeDir = new Vector(player.getEyeLocation().getDirection().getX(), 0, player.getEyeLocation().getDirection().getZ()).normalize();

        Vector shieldCenter = eyePos.add(eyeDir.clone().multiply(0.35));
        Vector upper = shieldCenter.clone().add(new Vector(eyeDir.getZ(), 0, -eyeDir.getX()).multiply(0.5));
        Vector lower = shieldCenter.clone().add(new Vector(-eyeDir.getZ(), 0, eyeDir.getX()).multiply(0.5)).add(new Vector(0, -1.5, 0));

        return BoundingBox.of(upper, lower);
    }

    @Override
    public Vector onHit(Entity ent, double damage, RayTraceTargetResult impact, Vector path, double distance, double velocity) {
        ShieldFeature.getInstance().getConfig().getImpactSound().play(impact.getRayTraceResult().getHitPosition().toLocation(player.getWorld()));

        return newPath(path);
    }

    private Vector newPath(Vector path) {
        if (ShieldFeature.getInstance().getConfig().isReflect()) {
            // Rd = id - 2 * dot(n, id) * n
            Vector normal = player.getEyeLocation().getDirection().normalize();
            return path.subtract(normal.clone().multiply(2*normal.dot(path)));
        } else {
            return path;
        }
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public double getPenetrationCost() {
        return ShieldFeature.getInstance().getConfig().getPenetrationCost();
    }

    @Override
    public double getRestitution() {
        return ShieldFeature.getInstance().getConfig().getRestitution();
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }
}
