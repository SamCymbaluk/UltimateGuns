package com.samcymbaluk.ultimateguns.features.guns;

import com.samcymbaluk.ultimateguns.util.NbtTags;
import net.minecraft.server.v1_14_R1.ItemMapEmpty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunAmmo {


    private String id;
    private Material material;
    private String name;
    private String caliber;
    private boolean individual;
    private int capacity;
    private int reloadTime;

    private transient GunCaliber gunCaliber;

    public static GunAmmo fromItem(ItemStack item) {
        String ammoType = NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_ammo_type");
        return GunFeature.getInstance().getConfig().getGunAmmo(ammoType);
    }

    public static int getAmmo(ItemStack item) {
        String ammoStr = NbtTags.getNBTData(item, GunFeature.GUN_NBT_KEY + "_ammo_amount");
        return ammoStr == null ? 0 : Integer.parseInt(ammoStr);
    }

    public GunAmmo(String id, Material material, String name, String caliber, boolean individual, int capacity, int reloadTime) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.caliber = caliber;
        this.individual = individual;
        this.capacity = capacity;
        this.reloadTime = reloadTime;
    }

    public ItemStack createItem(int ammo) {
        ItemStack ammoItem = new ItemStack(material, individual ? ammo : 1);
        ItemMeta im = ammoItem.getItemMeta();
        im.setDisplayName(getName(ammo));
        ammoItem.setItemMeta(im);
        ammoItem = NbtTags.setNBTData(ammoItem, GunFeature.GUN_NBT_KEY + "_ammo_type", id);
        ammoItem = NbtTags.setNBTData(ammoItem, GunFeature.GUN_NBT_KEY + "_ammo_amount", Integer.toString(ammo));

        return ammoItem;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getName(int ammo) {
        String str = ChatColor.translateAlternateColorCodes('&', name);
        str = str.replace(GunFeature.getInstance().getConfig().getCurrentAmmoPlaceholder(), Integer.toString(ammo));
        str = str.replace(GunFeature.getInstance().getConfig().getMaxAmmoPlaceholder(), Integer.toString(capacity));

        return str;
    }

    public GunCaliber getCaliber() {
        if (gunCaliber == null) {
            gunCaliber = GunFeature.getInstance().getConfig().getGunCaliber(caliber);
        }
        return gunCaliber;
    }

    public String getCaliberId() {
        return caliber;
    }

    public boolean isIndividual() {
        return individual;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getReloadTime() {
        return reloadTime;
    }
}
