package com.samcymbaluk.ultimateguns.features.guns;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.UltimateGunsPlayer;
import com.samcymbaluk.ultimateguns.features.guns.projectiles.GunProjectile;
import com.samcymbaluk.ultimateguns.util.NBTStoredValue;
import com.samcymbaluk.ultimateguns.util.NbtTags;
import com.samcymbaluk.ultimateguns.util.PlayerUtil;
import jdk.internal.jline.internal.Nullable;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
    private boolean auto = false;
    private double vertRecoil = 0;
    private double horiRecoil = 0;

    // NBT stored
    private NBTStoredValue<String> uuid;
    private NBTStoredValue<Integer> remainingAmmo;
    //private Integer remainingAmmo;
    private NBTStoredValue<GunAmmo> loadedAmmoType;
    //private GunAmmo loadedAmmoType;

    public Gun(GunSpecifications specifications, ItemStack item) {
        this(UUID.randomUUID().toString(), specifications, item);
    }

    public Gun(String uuid, GunSpecifications specifications, ItemStack item) {
        this.uuid = new NBTStoredValue<>(item, GunFeature.GUN_NBT_KEY + "_uuid", Function.identity(), Function.identity());
        this.uuid.set(uuid);
        this.specifications = specifications;
        this.item = item;

        this.loadedAmmoType = new NBTStoredValue<>(item, GunFeature.GUN_NBT_KEY + "_loaded_ammo_type",
                ammoType -> ammoType == null ? "" : ammoType.getId(),
                ammoStr -> GunFeature.getInstance().getConfig().getGunAmmo(ammoStr));

        this.remainingAmmo = new NBTStoredValue<>(item, GunFeature.GUN_NBT_KEY + "_remaining_ammo", ammo -> {
            String ammoStr = Integer.toString(ammo);
            ItemMeta im = this.item.getItemMeta();
            im.setDisplayName(this.specifications.getGunName() + this.specifications.getAmmoString(ammo, isLoaded()));
            this.item.setItemMeta(im);

            return ammoStr;
        }, s -> s.equals("") ? null : Integer.parseInt(s), 0);
    }

    /*
     * Firing
     */

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

        int remainingAmmo = this.remainingAmmo.get();
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

        if (remainingAmmo.get() < 1) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            return;
        }

        GunProjectile proj = loadedAmmoType.get().getCaliber().getProjectileType().getProjectile(this, loadedAmmoType.get().getCaliber(), gunPlayer.getPlayer());
        proj.fire();
        remainingAmmo.set(remainingAmmo.get() - 1);

        if (remainingAmmo.get() == 0 && loadedAmmoType.get().isIndividual()) {
            loadedAmmoType.set(null);
        }
    }

    /*
     * Reloading
     */

    public void load(UltimateGunsPlayer gunPlayer) {
        // Already loading
        if (reloadTime > 0) return;

        Player player = gunPlayer.getPlayer();
        ItemStack ammoItem = getAmmo(gunPlayer.getPlayer());

        if (isLoaded()) { // Unload
            unload(gunPlayer, ammoItem);
        } else { // Reload
            // Inv has valid ammo, begin reload
            if (ammoItem != null) {
                reload(gunPlayer, ammoItem);
            }
        }
    }

    private void unload(UltimateGunsPlayer gunPlayer, ItemStack ammoItem) {
        if (loadedAmmoType.get().isIndividual()) {
            // Only unload if we can't reload
            if (ammoItem == null) {
                ejectAmmo(gunPlayer);
            } else {
                reload(gunPlayer, ammoItem);
            }
        } else {
            ejectMag(gunPlayer);
        }
    }

    private void reload(UltimateGunsPlayer gunPlayer, ItemStack ammoItem) {
        Player player = gunPlayer.getPlayer();

        GunAmmo ammoType = GunAmmo.fromItem(ammoItem);

        if (ammoType.isIndividual()) {
            if (remainingAmmo.get() < this.getSpecifications().getIndividualAmmoCapacity()) {
                PlayerUtil.removeSingleItem(player, ammoItem);
                reloadTask(gunPlayer, ammoItem, ammoType, () -> insertAmmo(ammoType));
            }
        } else {
            PlayerUtil.removeSingleItem(player, ammoItem);
            reloadTask(gunPlayer, ammoItem, ammoType, () -> insertMeg(ammoItem, ammoType));
        }
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

    private void insertMeg(ItemStack ammoItem, GunAmmo ammoType) {
        this.loadedAmmoType.set(ammoType);
        this.remainingAmmo.set(GunAmmo.getAmmo(ammoItem));
    }

    private void insertAmmo(GunAmmo ammoType) {
        this.loadedAmmoType.set(ammoType);
        this.remainingAmmo.set(this.remainingAmmo.get() + 1);
    }

    private void ejectMag(UltimateGunsPlayer gunsPlayer) {
        GunAmmo ammoType = loadedAmmoType.get();
        loadedAmmoType.set(null);
        ItemStack mag = ammoType.createItem(remainingAmmo.get());
        remainingAmmo.set(0);

        PlayerUtil.safeAdd(gunsPlayer.getPlayer(), mag);
    }

    private void ejectAmmo(UltimateGunsPlayer gunsPlayer) {
        GunAmmo ammoType = loadedAmmoType.get();
        ItemStack ammo = ammoType.createItem(1);
        remainingAmmo.set(remainingAmmo.get() - 1);

        if (remainingAmmo.get() == 0) {
            loadedAmmoType.set(null);
        }

        PlayerUtil.safeAdd(gunsPlayer.getPlayer(), ammo);
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

    /*
     * Recoil and Accuracy
     */

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

    /*
     * Accessors
     */

    public GunSpecifications getSpecifications() {
        return specifications;
    }

    public String getUuid() {
        return uuid.get();
    }

    public boolean isLoaded() {
        return this.loadedAmmoType.get() != null;
    }
}
