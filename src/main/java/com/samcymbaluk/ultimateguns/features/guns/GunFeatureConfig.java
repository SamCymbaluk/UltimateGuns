package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.config.util.PostProcessable;
import com.samcymbaluk.ultimateguns.features.PluginFeatureConfig;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.ProjectileType;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GunFeatureConfig extends PluginFeatureConfig implements PostProcessable {

    private boolean debug = true;

    private List<String> reloadMessages = Arrays.asList(
            "&cReloading",
            "&aR&celoading",
            "&aRe&cloading",
            "&aRel&coading",
            "&aRelo&cading",
            "&aReloa&cding",
            "&aReload&cing",
            "&aReloadi&cng",
            "&aReloadin&cg",
            "&aReloading"
    );

    private List<GunCaliber> calibers = Arrays.asList(
        new GunCaliber("9mm","9mm", ProjectileType.BULLET,1, 4, 375, 0.02, 0.015, 12, 0, 2),
        new GunCaliber("556", "5.56×45mm", ProjectileType.BULLET, 1, 5, 960, 0.02, 0.01, 26, 0, 2),
        new GunCaliber("birdshot", "Birdshot", ProjectileType.BULLET, 15, 2, 350, 0.1, 0.02, 6, 0, 1)
    );

    private transient Map<String, GunCaliber> caliberMap = new HashMap<>();

    private List<GunAmmo> ammoItems = Arrays.asList(
        new GunAmmo("m4_mag", Material.GOLD_INGOT,"&7M4 Mag <&e@&7:&e#&7>", "556", false, 30, 40),
        new GunAmmo("glock_mag", Material.SLIME_BALL,"&7Glock Mag <&e@&7:&e#&7>", "9mm", false, 15, 25),
        new GunAmmo("556_bullet", Material.GOLD_NUGGET,"&75.56×45mm", "556", true, 1, 5),
        new GunAmmo("birdshot_shell", Material.WHEAT_SEEDS,"&7Birdshot Shell", "birdshot", true, 1, 10)
    );

    private transient Map<String, GunAmmo> ammoItemsMap = new HashMap<>();

    private List<GunSpecifications> guns = Arrays.asList(
        new GunSpecifications("556_gun", Material.GOLDEN_HOE, Collections.singleton("m4_mag")),
        new GunSpecifications("556_gun_individual", Material.GOLDEN_SHOVEL, Collections.singleton("556_bullet")),
        new GunSpecifications("glock", Material.WOODEN_HOE, Collections.singleton("glock_mag")),
        new GunSpecifications("shotgun", Material.WOODEN_AXE, Collections.singleton("birdshot_shell"))
    );

    private transient Map<Material, GunSpecifications> gunsMap = new HashMap<>();
    private transient Map<String, GunSpecifications> gunIdMap = new HashMap<>();


    private String currentAmmoPlaceholder = "@";
    private String maxAmmoPlaceholder = "#";

    private double gravity = 0.25;

    private transient Set<Material> ammoMaterials = new HashSet<>();

    @Override
    public void gsonPostProcess() {
        // Setup lookup hash tables
        for  (GunCaliber caliber : calibers) {
            Validate.isTrue(null == caliberMap.put(caliber.getId(), caliber), "Duplicate caliber id");
        }
        for (GunAmmo ammo : ammoItems) {
            Validate.isTrue(null == ammoItemsMap.put(ammo.getId(), ammo), "Duplicate ammo item id");
        }
        for (GunSpecifications specs : guns) {
            Validate.isTrue(null == gunsMap.put(specs.getMaterial(), specs), "Duplicate gun id");
            gunIdMap.put(specs.getId(), specs);
        }
        for (GunAmmo ammo : ammoItems) {
            ammoMaterials.add(ammo.getMaterial());
        }

        // Interpret color codes
        for (int i = 0; i < reloadMessages.size(); i++) {
            reloadMessages.set(i, ChatColor.translateAlternateColorCodes('&', reloadMessages.get(i)));
        }

        // Validate data
        for (GunSpecifications specs : guns) {
            for (String ammo : specs.getSupportedAmmo()) {
                Validate.isTrue(ammoItemsMap.containsKey(ammo), "Invalid ammo id");
            }
        }
        for (GunAmmo ammo : ammoItems) {
            Validate.isTrue(caliberMap.containsKey(ammo.getCaliberId()), "Invalid caliber id");
        }

    }

    public boolean isDebug() {
        return debug;
    }

    public String getReloadMessage(int current, int total) {
        int index = (current * (reloadMessages.size() - 1)) / total;
        return reloadMessages.get(index);
    }

    public boolean isGunMaterial(Material material) {
        return gunsMap.containsKey(material);
    }

    public boolean isAmmoMaterial(Material material) {
        return ammoMaterials.contains(material);
    }

    public GunSpecifications getGunSpecifications(Material mat) {
        return gunsMap.get(mat);
    }

    public GunSpecifications getGunSpecifications(String id) {
        return gunIdMap.get(id);
    }

    public GunCaliber getGunCaliber(String name) {
        return caliberMap.get(name);
    }

    public GunAmmo getGunAmmo(String name) {
        return ammoItemsMap.get(name);
    }

    public String getCurrentAmmoPlaceholder() {
        return currentAmmoPlaceholder;
    }

    public String getMaxAmmoPlaceholder() {
        return maxAmmoPlaceholder;
    }

    public double getGravity() {
        return gravity;
    }
}
