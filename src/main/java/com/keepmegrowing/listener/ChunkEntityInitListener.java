
package com.keepmegrowing.listener;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.storage.ChunkEntitySnapshotService;
import com.keepmegrowing.mob.EntityGrowthProcessor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkEntityInitListener
implements Listener {
    private ChunkEntitySnapshotService a = KeepMeGrowing.a().d();

    @EventHandler
    public void a(ChunkLoadEvent event) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(event.getWorld().getName())) {
            return;
        }
        org.bukkit.Chunk chunk = event.getChunk();
        for (Entity entity : chunk.getEntities()) {
            EntityGrowthProcessor.applyOfflineGrowth(entity);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void a(ChunkUnloadEvent chunkUnloadEvent) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(chunkUnloadEvent.getWorld().getName())) {
            return;
        }
        this.a.a(chunkUnloadEvent.getChunk());
    }
}
