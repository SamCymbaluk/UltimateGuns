package com.samcymbaluk.ultimateguns.grenades.frag;

import org.bukkit.Material;

public class FragFeatureConfig {

    private int fuse = 80;
    private int throwVelocity = 25;
    private Material itemMaterial = Material.COAL;
    private Material entityMaterial = Material.COAL_BLOCK;

    public int getFuse() {
        return fuse;
    }

    public int getThrowVelocity() {
        return throwVelocity;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public Material getEntityMaterial() {
        return entityMaterial;
    }
}
