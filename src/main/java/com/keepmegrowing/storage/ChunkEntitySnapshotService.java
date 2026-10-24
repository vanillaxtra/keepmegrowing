package com.keepmegrowing.storage;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.mob.EntityGrowthProcessor;
import com.keepmegrowing.growth.util.VersionMaterials;
import com.keepmegrowing.objects.ChunkKey;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


public final class ChunkEntitySnapshotService {
    private final Map<ChunkKey, Entity[]> snapshotByChunk = new HashMap<>();
    private BukkitTask pollTask;

    public ChunkEntitySnapshotService() {
        ChunkEntitySnapshotService self = this;
        this.pollTask =
                Bukkit.getScheduler()
                        .runTaskTimer(
                                (Plugin) KeepMeGrowing.a(),
                                () -> {
                                    KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
                                    for (World world : Bukkit.getWorlds()) {
                                        if (plugin != null
                                                && plugin.getGrowthConfig()
                                                        .isWorldBlacklisted(world.getName())) {
                                            continue;
                                        }
                                        for (Chunk chunk : world.getLoadedChunks()) {
                                            self.snapshotByChunk.put(
                                                    ChunkKey.fromChunk(chunk), chunk.getEntities());
                                        }
                                    }
                                },
                                20L,
                                20L);
    }

    public final void shutdown() {
        if (pollTask != null) {
            pollTask.cancel();
            pollTask = null;
        }
        this.snapshotByChunk.clear();
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
