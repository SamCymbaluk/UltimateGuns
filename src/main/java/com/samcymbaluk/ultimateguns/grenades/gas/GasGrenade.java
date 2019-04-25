package com.samcymbaluk.ultimateguns.grenades.gas;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.grenades.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GasGrenade extends Grenade {

    private GasManager gm;

    private int TASK_ID;
    private int tick = 0;
    private boolean exploded = false;

    public GasGrenade(GasManager gasManager, Player thrower) {
        super(thrower, 25, Material.IRON_BLOCK);
        this.gm = gasManager;
    }

    @Override
    public boolean onImpact(Vector path, Location loc) {
        return false;
    }

    @Override
    public void onTick(Location loc, int tick) {
        if (tick >= 80 && !exploded) { //Explode
            loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 1, 2);
            loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.5F);
            explode(loc);
            exploded = true;
        }
    }

    private void explode(final Location loc) {
        //Slowly release gas
        TASK_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(UltimateGuns.getInstance(), () -> {
            tick++;
            if (tick == 200) {
                Bukkit.getScheduler().cancelTask(TASK_ID);
                getProjectile().end();
            }
            new GasBlock(gm, getProjectile().getLocation().getBlock(), 6);
        }, 0, 1);
    }


}
