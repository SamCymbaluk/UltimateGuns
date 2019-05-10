package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.features.PluginFeatureConfig;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.ProjectileType;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class GunFeatureConfig extends PluginFeatureConfig {

    private Map<String, GunCaliber> caliberTypes = new HashMap<String, GunCaliber>(){{
        put("9mm", new GunCaliber("9mm", ProjectileType.BULLET,1, 4, 375, 0.02, 1, 10, 0, 2));
        put("556", new GunCaliber("5.56×45mm", ProjectileType.BULLET, 1, 5, 960, 0.02, 1, 26, 0, 2));
    }};

    private Map<Material, GunSpecifications> guns = new HashMap<Material, GunSpecifications>(){{
        put(Material.GOLD_INGOT, new GunSpecifications("556"));
    }};
}
