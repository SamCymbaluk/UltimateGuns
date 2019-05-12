package com.samcymbaluk.ultimateguns;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UltimateGunsPlayer {

    private Player player;

    private Location lastLoc;
    private Location currLoc;
    private boolean scoped = false;

    public UltimateGunsPlayer(Player player) {
        this.player = player;
    }

    public boolean isOnGround() {
        return (player.getLocation().add(0, -0.1, 0).getBlock().getType().isSolid()
                || player.getLocation().add(-0.3, -0.1, -0.3).getBlock().getType().isSolid()
                || player.getLocation().add(-0.3, -0.1, 0.3).getBlock().getType().isSolid()
                || player.getLocation().add(0.3, -0.1, 0.3).getBlock().getType().isSolid()
                || player.getLocation().add(-0.3, -0.1, 0.3).getBlock().getType().isSolid());
    }

    public Player getPlayer() {
        return player;
    }

    public double getSpeedSquared() {
        if (lastLoc != null && currLoc != null) {
            return lastLoc.distanceSquared(currLoc);
        } else {
            return 0;
        }
    }

    public double getSpeed() {
        return Math.sqrt(getSpeedSquared());
    }

    void updateLoc() {
        lastLoc = currLoc;
        currLoc = player.getLocation();
    }

    public boolean isScoped() {
        return scoped;
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }
}
