package com.samcymbaluk.ultimateguns.util;

import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class NBTStoredValue<T> {

    private ItemStack item;
    private String key;
    private Function<T, String> serializer;
    private Function<String, T> deserializer;

    // Cache
    private T value = null;

    public NBTStoredValue(ItemStack item, String key, Function<T, String> serializer, Function<String, T> deserializer) {
        this.item = item;
        this.key = key;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public NBTStoredValue(ItemStack item, String key, Function<T, String> serializer, Function<String, T> deserializer, T defaultValue) {
        this.item = item;
        this.key = key;
        this.serializer = serializer;
        this.deserializer = deserializer;
        if (this.get() == null) this.set(defaultValue);
    }

    public T get() {
        if (this.value == null) {
            value = deserializer.apply(NbtTags.getNBTData(item, key));
        }
        return value;
    }

    public void set(T value) {
        this.value = value;
        NbtTags.setNBTData(item, key, serializer.apply(value));
    }
}
