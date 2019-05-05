package com.samcymbaluk.ultimateguns.features.grenades.gas;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.config.util.ConfigSound;
import com.samcymbaluk.ultimateguns.features.grenades.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GasGrenade extends Grenade {

    private GasManager gm;
    private GasFeature gasFeature;
    private GasFeatureConfig conf;

    private int TASK_ID;
    private int tick = 0;
    private boolean exploded = false;

    public GasGrenade(GasManager gasManager, GasFeature gasFeature, Player thrower) {
        super(thrower, gasFeature.getConfig().getThrowVelocity(), gasFeature.getConfig().getEntityMaterial(), gasFeature.getConfig());
        this.gm = gasManager;
        this.gasFeature = gasFeature;
        this.conf = gasFeature.getConfig();
    }

    @Override
    public boolean onImpact(Vector path, Location loc) {
        return false;
    }

    @Override
    public void onTick(Location loc, int tick) {
        if (tick >= conf.getFuse() && !exploded) { //Explode
            ConfigSound.playAll(conf.getReleaseSounds(), loc);
            explode(loc);
            exploded = true;
        }
    }

    private void explode(final Location loc) {
        //Slowly release gas
        TASK_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(UltimateGuns.getInstance(), () -> {
            tick++;
            if (tick >= conf.getFuse() + conf.getReleaseDuration()) {
                Bukkit.getScheduler().cancelTask(TASK_ID);
                getProjectile().end();
            }
            if (tick % conf.getReleasePeriod() == 0) {
                new GasBlock(gm, getProjectile().getLocation().getBlock(), conf.getReleaseAmount());
            }
        }, 0, 1);
    }


}
