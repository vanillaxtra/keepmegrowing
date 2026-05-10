package com.keepmegrowing.objects;

import com.google.gson.annotations.Expose;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class PlantBlock {
    @Expose
    public final int blockX;
    @Expose
    public final int blockY;
    @Expose
    public final int blockZ;

    public PlantBlock(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    public final Block getBlock(World world) {
        return world.getBlockAt(this.blockX, this.blockY, this.blockZ);
    }

    public static PlantBlock fromLocation(Location location) {
        return new PlantBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int hashCode() {
        int h = 237 + this.blockX;
        h = h * 79 + this.blockY;
        h = h * 79 + this.blockZ;
        return h;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PlantBlock that = (PlantBlock) other;
        if (this.blockX != that.blockX) {
            return false;
        }
        if (this.blockY != that.blockY) {
            return false;
        }
        return this.blockZ == that.blockZ;
    }
}
