package com.samcymbaluk.ultimateguns.util;

import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import java.lang.reflect.Field;
import java.util.UUID;

public class NbtTags {

    public static NBTTagCompound getTag(org.bukkit.inventory.ItemStack item) {
        if (item instanceof CraftItemStack) {
            try {
                Field field = CraftItemStack.class.getDeclaredField("handle");
                field.setAccessible(true);
                return ((ItemStack) field.get(item)).getTag();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static org.bukkit.inventory.ItemStack setTag(org.bukkit.inventory.ItemStack item, NBTTagCompound tag) {
        CraftItemStack craftItem = null;
        if (item instanceof CraftItemStack) {
            craftItem = (CraftItemStack) item;
        } else {
            craftItem = CraftItemStack.asCraftCopy(item);
        }

        ItemStack nmsItem = null;
        try {
            Field field = CraftItemStack.class.getDeclaredField("handle");
            field.setAccessible(true);
            nmsItem = ((ItemStack) field.get(item));
        } catch (Exception e) {
        }
        if (nmsItem == null) {
            nmsItem = CraftItemStack.asNMSCopy(craftItem);
        }

        nmsItem.setTag(tag);
        try {
            Field field = CraftItemStack.class.getDeclaredField("handle");
            field.setAccessible(true);
            field.set(craftItem, nmsItem);
        } catch (Exception e) {
        }

        return craftItem;
    }

    public static org.bukkit.inventory.ItemStack setUnstackable(org.bukkit.inventory.ItemStack item) {
        item = setNBTData(item, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        return item;
    }

    public static org.bukkit.inventory.ItemStack setUnbreakable(org.bukkit.inventory.ItemStack item) {
        if (item == null) return item;
        if (!(item instanceof CraftItemStack)) {
            item = CraftItemStack.asCraftCopy(item);
        }
        NBTTagCompound tag = getTag(item);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setByte("Unbreakable", (byte) 1);
        return setTag(item, tag);
    }

    public static org.bukkit.inventory.ItemStack setNBTData(org.bukkit.inventory.ItemStack item, String key, String value) {
        if (!(item instanceof CraftItemStack)) {
            item = CraftItemStack.asCraftCopy(item);
        }
        NBTTagCompound tag = getTag(item);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        return setTag(item, tag);
    }

    public static String getNBTData(org.bukkit.inventory.ItemStack item, String key) {
        if (!(item instanceof CraftItemStack)) {
            item = CraftItemStack.asCraftCopy(item);
        }
        NBTTagCompound tag = getTag(item);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        return tag.getString(key);
    }
}
