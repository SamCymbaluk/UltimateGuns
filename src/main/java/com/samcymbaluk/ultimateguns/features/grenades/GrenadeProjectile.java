package com.samcymbaluk.ultimateguns.features.grenades;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.RayTraceTargetResult;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.targets.BlockTarget;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EnumItemSlot;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_14_R1.Vector3f;
import net.minecraft.server.v1_14_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public class GrenadeProjectile {

    private Material material;
    private Entity ent;
    private Location loc;
    private Grenade grenade;
    private GrenadeFeatureConfig config;

    private int tick = 0;
    private EntityArmorStand stand;

    private boolean ended = false;

    public GrenadeProjectile(Entity ent, Location loc, Grenade grenade, Material material, GrenadeFeatureConfig config) {
        this.material = material;
        this.ent = ent;
        this.loc = loc.clone();
        this.grenade = grenade;
        this.config = config;
    }

    public void start(Location start, double velocity) {
        WorldServer s = ((CraftWorld) ent.getLocation().getWorld()).getHandle();

        stand = new EntityArmorStand(s, ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ());
        stand.setInvisible(true);
        stand.setRightArmPose(new Vector3f(0, -90F,  -18F));

        PacketPlayOutSpawnEntityLiving create = new PacketPlayOutSpawnEntityLiving(stand);
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(stand.getId(), EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(material, 1)));
        for (Player p : ent.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(create);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(equipment);
        }

        step(ent.getLocation().getDirection().multiply(velocity / 20.0), start);
    }

    public Location getLocation() {
        return this.loc;
    }

    private void step(Vector path, Location start) {
        this.loc = start;
        grenade.onTick(start, tick);

        RayTraceTargetResult rtResult = Target.rayTrace(start, path, path.length(), rayTracePredicate());
        Location newStart = rtResult != null
                ? rtResult.getRayTraceResult().getHitPosition().toLocation(start.getWorld())
                : start.add(path.clone().normalize().multiply(path.length()));

        stand.setLocation(newStart.getX(), newStart.getY() - 0.75, newStart.getZ(), 0.0F, 0.0F);

        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(stand);
        for (Player p : ent.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(teleport);
        }

        //Hit a block, do impact calculation
        if (rtResult != null) {

            //If grenade life should not be terminated
            if (!grenade.onImpact(path.clone(), newStart.clone())) {
                impactCalculations(path, rtResult);
                if (path.length() > 0.1) {
                    config.getImpactSound().play(newStart);
                }
            } else { //Remove grenade
                for (Player p : ent.getWorld().getPlayers()) {
                    PacketPlayOutEntityDestroy kill = new PacketPlayOutEntityDestroy(stand.getId());
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(kill);
                }
                return;
            }
        }

        //Apply water drag
        if (newStart.getBlock().isLiquid()) {
            if (path.length() > 0.1) {
                path.multiply(config.getLiquidMultiplier());
            }
        }

        //Gravity
        if (!newStart.clone().add(0.0, -0.01, 0.0).getBlock().getType().isSolid()) {
            path.add(new Vector(0,  -config.getGravity(), 0));
        }

        if (!ended) {
            tick += rtResult != null ? 3 : 1;
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> step(path, newStart), rtResult != null ? 3 : 1);
        } else {
            for (Player p : ent.getWorld().getPlayers()) {
                PacketPlayOutEntityDestroy kill = new PacketPlayOutEntityDestroy(stand.getId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(kill);
            }
        }
    }

    private Predicate<Target> rayTracePredicate() {
        return target -> {
            if (target instanceof BlockTarget) {
                BlockTarget bt = (BlockTarget) target;
                return bt.getBlock().isPassable();
            } else if (target instanceof LivingEntityTarget) {
                LivingEntityTarget et = (LivingEntityTarget) target;
                return et.getEntity().equals(grenade.getThrower());
            }
            return false;
        };
    }

    /**
     * Does the necessary impact calculations on the path vector
     * @param path
     * @param rtResult
     */
    private void impactCalculations(Vector path, RayTraceTargetResult rtResult) {

        BlockFace blockFace = rtResult.getRayTraceResult().getHitBlockFace();
        Target target = rtResult.getTarget();

        if (blockFace != null) {

            Vector hitPlain = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());

            //Negate impact component
            path.setX(hitPlain.getX() != 0.0 ? -path.getX() : path.getX());
            path.setY(hitPlain.getY() != 0.0 ? -path.getY() : path.getY());
            path.setZ(hitPlain.getZ() != 0.0 ? -path.getZ() : path.getZ());
        }
        path.multiply(target.getRestitution());
    }

    public void end() {
        ended = true;
    }
}
