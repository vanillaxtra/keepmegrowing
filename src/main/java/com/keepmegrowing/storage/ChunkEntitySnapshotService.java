package com.keepmegrowing.storage;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.mob.EntityGrowthProcessor;
import com.keepmegrowing.objects.ChunkKey;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class ChunkEntitySnapshotService {
    private final Map<ChunkKey, Entity[]> snapshotByChunk = new HashMap<>();

    public ChunkEntitySnapshotService() {
        ChunkEntitySnapshotService self = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks()) {
                        self.snapshotByChunk.put(ChunkKey.fromChunk(chunk), chunk.getEntities());
                    }
                }
            }
        }.runTaskTimer((Plugin) KeepMeGrowing.a(), 20L, 20L);
    }

    public final void a(Chunk chunk) {
        ChunkKey key = ChunkKey.fromChunk(chunk);
        Entity[] entityArray = this.snapshotByChunk.get(key);
        if (entityArray == null) {
            return;
        }
        for (Entity entity : entityArray) {
            EntityGrowthProcessor.markEntityUnloaded(entity);
        }
        this.snapshotByChunk.remove(key);
    }
}
