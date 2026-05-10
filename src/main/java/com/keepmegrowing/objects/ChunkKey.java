package com.keepmegrowing.objects;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkKey {
    public final int chunkX;
    public final int chunkZ;
    public final String worldName;

    public ChunkKey(int chunkX, int chunkZ, String worldName) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.worldName = worldName;
    }

    @Override
    public int hashCode() {
        int h = 203 + this.chunkX;
        h = h * 29 + this.chunkZ;
        h = h * 29 + this.worldName.hashCode();
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
        ChunkKey that = (ChunkKey) other;
        if (this.chunkX != that.chunkX) {
            return false;
        }
        if (this.chunkZ != that.chunkZ) {
            return false;
        }
        return this.worldName.equals(that.worldName);
    }

    public static ChunkKey fromChunk(Chunk chunk) {
        return new ChunkKey(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    @Override
    public String toString() {
        return this.worldName + "," + this.chunkX + "," + this.chunkZ;
    }

    public boolean isChunkLoaded() {
        World w = Bukkit.getWorld(this.worldName);
        return w != null && w.isChunkLoaded(this.chunkX, this.chunkZ);
    }
}
