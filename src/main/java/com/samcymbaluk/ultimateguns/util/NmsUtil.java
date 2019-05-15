package com.samcymbaluk.ultimateguns.util;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffectType;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

import java.lang.reflect.Field;

public class NmsUtil {

    public static Sound blockSound(org.bukkit.block.Block block) {
        try {
            World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();
            Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
            SoundEffectType soundEffectType = nmsBlock.getStepSound();

            Field breakSound = SoundEffectType.class.getDeclaredField("q");
            breakSound.setAccessible(true);
            SoundEffect nmsSound = (SoundEffect) breakSound.get(soundEffectType);

            Field keyField = SoundEffect.class.getDeclaredField("a");
            keyField.setAccessible(true);
            MinecraftKey nmsString = (MinecraftKey) keyField.get(nmsSound);

            return Sound.valueOf(nmsString.getKey().replace(".", "_").toUpperCase());
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
