package com.samcymbaluk.ultimateguns.util;

import org.bukkit.block.Block;

public class ImmutableBlockVector {

    private final int x;
    private final int y;
    private final int z;

    public ImmutableBlockVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ImmutableBlockVector(Block b) {
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutableBlockVector)) return false;
        ImmutableBlockVector other = (ImmutableBlockVector) obj;
        if (other.getX() == x && other.getY() == y && other.getZ() == z) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = 37 + x;
        result = 37 + result * y;
        result = 37 + result * z;
        return result;
    }
}