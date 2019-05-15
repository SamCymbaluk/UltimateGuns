package com.samcymbaluk.ultimateguns.features.guns;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.UltimateGunsPlayer;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.GunProjectile;
import com.samcymbaluk.ultimateguns.util.NbtTags;
import com.samcymbaluk.ultimateguns.util.PlayerUtil;
import jdk.internal.jline.internal.Nullable;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Gun {

    private static final double VERTICAL_RECOIL = 1.0 / 64.0;
    private static final double HORIZONTAL_RECOIL = 1.0 / 64.0;
    private static final double RECOIL_RESET_TICKS = 5;

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

            gun = uuid.equals("") ? new Gun(specs, item) : new Gun(uuid, specs, item);
            loadedGuns.put(gun.getUuid(), gun);
        }

        // Sometimes the item can get desynced. Updating item every time the gun is loaded is a easy solution
        gun.item = item;
        return gun;
    }

    private ItemStack item;

    private GunSpecifications specifications;

    // Transient
    private long lastFired = 0;
    private long lastClick = 0;
    private int reloadTime = 0;
    private int reloadAmount = 0;
    private boolean auto = false;
    private double vertRecoil = 0;
    private double horiRecoil = 0;

    // NBT stored
    private String uuid;
    private Integer remainingAmmo;
    private GunAmmo loadedAmmoType;

    public Gun(GunSpecifications specifications, ItemStack item) {
        this.specifications = specifications;
        this.item = item;

        setUuid(UUID.randomUUID().toString());
        setRemainingAmmo(0);
    }

    public Gun(String uuid, GunSpecifications specifications, ItemStack item) {
        this.uuid = uuid;
        this.specifications = specifications;
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

        int remainingAmmo = getRemainingAmmo();
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

        if (getRemainingAmmo() < 1) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            return;
        }

        GunProjectile proj = getLoadedAmmoType().getCaliber().getProjectileType().getProjectile(this, getLoadedAmmoType().getCaliber(), gunPlayer.getPlayer());
        proj.fire();
        setRemainingAmmo(getRemainingAmmo() - 1);
    }

    public void reload(UltimateGunsPlayer gunPlayer) {
        // Already loading
        if (reloadTime > 0) return;
        Player player = gunPlayer.getPlayer();

        // Locate ammo in inv
        ItemStack ammoItem = getAmmo(gunPlayer.getPlayer());

        ejectMag(gunPlayer);

        // Inv has valid ammo, begin reload
        if (ammoItem != null) {
            player.getInventory().removeItem(ammoItem);
            GunAmmo ammoType = GunAmmo.fromItem(ammoItem);

            reloadTask(gunPlayer, ammoItem, ammoType, () -> {
                insertMeg(gunPlayer, ammoItem, ammoType);
            });
        }
    }

    private ItemStack getAmmo(Player player) {
        for (int i = 35; i >= 0; i--) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;

            if (GunFeature.getInstance().getConfig().isAmmoMaterial(item.getType())) {
                GunAmmo ammo = GunAmmo.fromItem(item);
                if (ammo != null && getSpecifications().isSupported(ammo)) {
                    return item;
                }
            }
        }
        return null;
    }

    private void reloadTask(UltimateGunsPlayer gunsPlayer, ItemStack ammoItem, GunAmmo ammoType, Runnable callback) {
        if (reloadTime == ammoType.getReloadTime()) {
            callback.run();
            reloadTime = 0;
            return;
        }

        if (gunsPlayer.getPlayer().getInventory().getItemInMainHand().equals(this.item)) {
            reloadTime++;
            gunsPlayer.getPlayer().sendMessage("tick: " + reloadTime);
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGuns.getInstance(), () -> reloadTask(gunsPlayer, ammoItem, ammoType, callback), 1);
        } else {
            gunsPlayer.getPlayer().sendMessage("Loading cancelled");
            reloadTime = 0;
            PlayerUtil.safeAdd(gunsPlayer.getPlayer(), ammoItem);
        }
    }

    private void ejectMag(UltimateGunsPlayer gunsPlayer) {
        if (getLoadedAmmoType() != null && !getLoadedAmmoType().isIndividual()) {
            GunAmmo ammoType = getLoadedAmmoType();
            setLoadedAmmoType((GunAmmo) null);
            ItemStack mag = ammoType.createItem(getRemainingAmmo());
            setRemainingAmmo(0);

            PlayerUtil.safeAdd(gunsPlayer.getPlayer(), mag);
        }
    }

    private void insertMeg(UltimateGunsPlayer gunsPlayer, ItemStack ammoItem, GunAmmo ammoType) {
        setLoadedAmmoType(ammoType);
        setRemainingAmmo(GunAmmo.getAmmo(ammoItem));
    }

    public void applyAccuracy(Vector path, UltimateGunsPlayer gunsPlayer) {
        boolean moving = false;
        boolean jumping = !gunsPlayer.isOnGround();

        if (!gunsPlayer.getPlayer().isSneaking()) {
            if (gunsPlayer.getSpeedSquared() > 0.04) moving = true;
        }

        if (jumping) {
            path.add(new Vector(
                    (2 * Math.random() * specifications.getJumpingAccuracy()) - (specifications.getJumpingAccuracy()),
                    (2 * Math.random() * specifications.getJumpingAccuracy()) - (specifications.getJumpingAccuracy()),
                    (2 * Math.random() * specifications.getJumpingAccuracy()) - (specifications.getJumpingAccuracy())));
        } else if (moving) {
            path.add(new Vector(
                    (2 * Math.random() * specifications.getMovingAccuracy()) - (specifications.getMovingAccuracy()),
                    (2 * Math.random() * specifications.getMovingAccuracy()) - (specifications.getMovingAccuracy()),
                    (2 * Math.random() * specifications.getMovingAccuracy()) - (specifications.getMovingAccuracy())));
        } else {
            path.add(new Vector(
                    (2 * Math.random() * specifications.getAccuracy()) - (specifications.getAccuracy()),
                    (2 * Math.random() * specifications.getAccuracy()) - (specifications.getAccuracy()),
                    (2 * Math.random() * specifications.getAccuracy()) - (specifications.getAccuracy())));
        }
    }

    public void applyRecoil(Vector path) {
        Vector horiRecoilDir = new Vector(path.getZ(), 0, -path.getX());
        Vector vertRecoilDir = path.getCrossProduct(horiRecoilDir);

        horiRecoilDir.normalize();
        vertRecoilDir.normalize();

        path.add(vertRecoilDir.multiply(vertRecoil));
        path.add(horiRecoilDir.multiply(horiRecoil));

        vertRecoil += (Math.random() * VERTICAL_RECOIL);
        horiRecoil += (2 * Math.random() * HORIZONTAL_RECOIL) - (HORIZONTAL_RECOIL);
    }

    public GunSpecifications getSpecifications() {
        return specifications;
    }

    public String getUuid() {
        return uuid;
    }


    private void setUuid(String uuid) {
        this.uuid = uuid;
        item = NbtTags.setNBTData(item, GunFeature.GUN_NBT_KEY + "_uuid", uuid);
    }

    public int getRemainingAmmo() {
        if (remainingAmmo == null) {
            remainingAmmo = Integer.parseInt(NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_remaining_ammo"));
        }
        return remainingAmmo;
    }

    public void setRemainingAmmo(int ammo) {
        remainingAmmo = ammo;
        item = NbtTags.setNBTData(item, GunFeature.GUN_NBT_KEY + "_remaining_ammo", Integer.toString(ammo));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(specifications.getGunName() + specifications.getAmmoString(ammo, isLoaded()));
        item.setItemMeta(im);
    }

    public boolean isLoaded() {
        return getLoadedAmmoType() != null;
    }

    @Nullable
    public GunAmmo getLoadedAmmoType() {
        if (loadedAmmoType == null) {
            loadedAmmoType = GunFeature.getInstance().getConfig().getGunAmmo(NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_loaded_ammo_type"));
        }
        return loadedAmmoType;
    }

    public void setLoadedAmmoType(String ammoType) {
        GunAmmo ammo = GunFeature.getInstance().getConfig().getGunAmmo(ammoType);
        Validate.notNull(ammo, "Invalid ammo type");
        setLoadedAmmoType(ammo);
    }

    public void setLoadedAmmoType(GunAmmo ammoType) {
        loadedAmmoType = ammoType;
        item = NbtTags.setNBTData(item, GunFeature.GUN_NBT_KEY + "_loaded_ammo_type", ammoType == null ? "" : ammoType.getId());
    }

}
