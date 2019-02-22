package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.UltimateGunsProjectile;
import com.samcymbaluk.ultimateguns.targets.BlockTarget;
import com.samcymbaluk.ultimateguns.targets.LivingEntityTarget;
import com.samcymbaluk.ultimateguns.targets.Target;
import com.samcymbaluk.ultimateguns.util.ProjectileCallback;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class FragListener implements Listener {

    private FragFeature fragFeature;

    public FragListener(FragFeature fragFeature) {
        this.fragFeature = fragFeature;
        UltimateGuns.getInstance().getServer().getPluginManager().registerEvents(this, UltimateGuns.getInstance());
    }

    @EventHandler
    public void onPlayerIteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!fragFeature.isEnabled(player.getWorld())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();

            // TODO make configurable
            if (item.getType() == Material.COAL) {
                event.setCancelled(true);
                Frag frag = new Frag(player);
                frag.throwGrenade();
            }

            // TODO remove debug
            if (item.getType() == Material.GOLD_INGOT) {
                UltimateGunsProjectile proj = new UltimateGunsProjectile(player, true, 715./20., 0.25, 255);
                proj.start(player.getEyeLocation(), player.getEyeLocation().getDirection(), new ProjectileCallback() {
                    double penLeft = 70;

                    @Override
                    public Vector handleImpact(RayTraceResult impact, Target target, Vector path) {
                        if (target instanceof LivingEntityTarget) {
                            target.onHit(event.getPlayer(), 100);
                        } else if (target instanceof BlockTarget) {
                            penLeft -= 25;
                            if (penLeft <= 0) proj.kill();
                            path.multiply(((float) (30 - 25)) / ((float) 30));
                            Vector deflection = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
                            path.add(deflection.normalize().multiply(Math.sqrt(25) / 2));
                            return path;
                        }
                        return path;
                    }

                    @Override
                    public void handleStep(Location start, Vector path, double velocity) {
                        proj.debugProjectileEffect(start.toVector(), start.toVector().add(path));
                    }

                    @Override
                    public void done(Location end) {

                    }
                });
            }
        }
    }
}
