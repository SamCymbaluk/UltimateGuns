package com.samcymbaluk.ultimateguns.features.guns;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.UltimateGunsPlayer;
import com.samcymbaluk.ultimateguns.util.NbtTags;
import jdk.internal.jline.internal.Nullable;
import org.apache.commons.lang.Validate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Gun {

    private static final double VERTICAL_RECOIL = 64;
    private static final double HORIZONTAL_RECOIL = 64;
    private static final double RECOIL_RESET_TICKS = 4;

    private static Cache<String, Gun> loadedGuns = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    @Nullable
    public static Gun loadGun(ItemStack item) {
        Validate.notNull(item, "Item cannot be null");
        String uuid = NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_uuid");

        Gun gun = loadedGuns.getIfPresent(uuid);

        if (gun == null) {
            GunSpecifications specs = GunFeature.getInstance().getConfig().getGunSpecifications(item.getType());
            if (specs == null) return null;

            GunCaliber caliber = GunFeature.getInstance().getConfig().getGunCaliber(specs.getCaliber());
            if (caliber == null) return null;

            gun = uuid.equals("") ? new Gun(specs, caliber, item) : new Gun(uuid, specs, caliber, item);
            loadedGuns.put(gun.getUuid(), gun);
        }

        // Sometimes the item can get desynced. Updating item every time the gun is loaded is a easy solution
        gun.item = item;
        return gun;
    }

    private ItemStack item;

    // Transient
    private long lastFired = 0;
    private long lastClick = 0;
    private int reloadTime = 0;
    private int reloadAmount = 0;
    private boolean auto = false;
    private double vertRecoil = 0;
    private double horiRecoil = 0;

    private String uuid;
    private GunSpecifications specifications;
    private GunCaliber caliber;

    private int shotsFired = 0;

    public Gun(GunSpecifications specifications, GunCaliber caliber, ItemStack item) {
        this.specifications = specifications;
        this.caliber = caliber;
        this.item = item;

        setUuid(UUID.randomUUID().toString());
        setAmmo(0);
    }

    public Gun(String uuid, GunSpecifications specifications, GunCaliber caliber, ItemStack item) {
        this.uuid = uuid;
        this.specifications = specifications;
        this.caliber = caliber;
        this.item = item;
    }

    public void handleClick(PlayerInteractEvent event) {
        long tick = UltimateGuns.getInstance().getTick();
        Player player = event.getPlayer();

        //Reset recoil
        double ticks = tick - lastFired;
        if (ticks >= RECOIL_RESET_TICKS) {
            vertRecoil = horiRecoil = 0;
        }

        if (specifications.isAuto()) {
            auto = (tick - lastClick) >= 3 && (tick - lastClick) <= 5;
        }

        int remainingAmmo = getAmmo();
        if (remainingAmmo == 0) player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        if ((tick - lastFired) >= specifications.getBulletDelay() && remainingAmmo > 0) {
            fire(player);
            lastFired = tick;
            /*
            if (specifications.getRoundsPerBurst() > 1) {
                new BurstShot(this, player, gunSpecifications.roundsPerBurst).runTaskTimer(Crisis.getInstance(), 0, 1);
            } else if (isAuto()) {
                int bulletDelay = gunSpecifications.bulletDelay;
                new DelayShot(crisisGuns, this, player, (4 / bulletDelay) - 1).runTaskTimer(Crisis.getInstance(), bulletDelay, bulletDelay);
            }*/
        }

        lastClick = tick;
    }

    public void fire(Player player) {
        fire(UltimateGuns.getInstance().getGunPlayer(player));
    }

    public void fire(UltimateGunsPlayer gunPlayer) {
        Player player = gunPlayer.getPlayer();

        if (getAmmo() < 1) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            return;
        }

        getCaliber().getProjectileType().getProjectile(this, gunPlayer.getPlayer()).fire();
        setAmmo(getAmmo() - 1);
    }

    public void reload(UltimateGunsPlayer gunPlayer) {
        // TODO
        setAmmo(specifications.getAmmoSize());
    }

    public int getAmmo() {
        return Integer.parseInt(NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_ammo"));
    }

    public void setAmmo(int ammo) {
        NbtTags.setNBTData(item, GunFeature.GUN_NBT_KEY + "_ammo", Integer.toString(ammo));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(specifications.getGunName() + specifications.getAmmoString(ammo));
        this.item.setItemMeta(im);
    }

    public String getUuid() {
        return uuid;
    }


    public GunSpecifications getSpecifications() {
        return specifications;
    }

    public GunCaliber getCaliber() {
        return caliber;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    private void setUuid(String uuid) {
        this.uuid = uuid;
        NbtTags.setNBTData(item, GunFeature.GUN_NBT_KEY + "_uuid", uuid);
    }

}
