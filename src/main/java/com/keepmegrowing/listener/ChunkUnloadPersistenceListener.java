
package com.keepmegrowing.listener;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.objects.ChunkKey;
import com.keepmegrowing.objects.PlantChunkObject;
import com.keepmegrowing.storage.PlantChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadPersistenceListener
implements Listener {
    private PlantChunkStorage storage = KeepMeGrowing.a().b();

    @EventHandler
    public void a(ChunkLoadEvent chunkLoadEvent) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(chunkLoadEvent.getWorld().getName())) {
            return;
        }
        this.storage.c(ChunkKey.fromChunk(chunkLoadEvent.getChunk()));
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void a(ChunkUnloadEvent event) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(event.getWorld().getName())) {
            return;
        }
        ChunkKey key = ChunkKey.fromChunk(event.getChunk());
        if (KeepMeGrowing.a().f().isUnloadChunkCropScanEnabled()) {
            this.storage.scanChunkForTrackedCropMaterials(
                    event.getChunk(), KeepMeGrowing.a().c().getRegisteredMaterials());
        }
        PlantChunkObject pco = this.storage.b(key);
        if (pco != null && !this.storage.pendingUnloadAfterGrowth.contains(pco)) {
            if (this.storage.growthProcessingCooldown.containsKey(pco)
                    || this.storage.chunkLoadQueue.contains(pco)) {
                this.storage.pendingUnloadAfterGrowth.add(pco);
                return;
            }
            pco.handleChunkUnload();
        }
    }
}
