package com.keepmegrowing.storage;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.databaseapi.database.Database;
import com.keepmegrowing.mob.EntityGrowthProcessor;
import com.keepmegrowing.objects.ChunkKey;
import com.keepmegrowing.objects.PlantBlock;
import com.keepmegrowing.objects.PlantChunkObject;
import com.keepmegrowing.integration.worldguard.KeepMeGrowingWorldGuard;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class PlantChunkStorage {
    private final Map<ChunkKey, PlantChunkObject> chunkObjectsByKey;
    private final Set<PlantChunkObject> chunksMarkedForSqlDelete;
    private final Database<PlantChunkObject> plantDatabase;
    private final KeepMeGrowing plugin;
    private BukkitTask chunkPollTask;
    
    public final Queue<PlantChunkObject> chunkLoadQueue = new ConcurrentLinkedQueue<>();
    
    public final Set<PlantChunkObject> pendingUnloadAfterGrowth = new HashSet<>();
    
    public final Map<PlantChunkObject, Long> growthProcessingCooldown = new HashMap<>();

    public PlantChunkStorage(KeepMeGrowing plugin) {
        this.plugin = plugin;
        this.plantDatabase =
                new Database<PlantChunkObject>(
                        KeepMeGrowing.a().g().getSettings(), PlantChunkObject.class);
        this.chunkObjectsByKey = new HashMap<>();
        this.chunksMarkedForSqlDelete = new HashSet<>();
        this.chunkPollTask =
                Bukkit.getScheduler()
                        .runTaskTimer(
                                (Plugin) KeepMeGrowing.a(),
                                () -> {
                                    if (!KeepMeGrowing.a().isEnabled()) {
                                        this.chunkPollTask.cancel();
                                        return;
                                    }
                                    if (KeepMeGrowing.a().getGrowthRateController().getEffectiveMultiplier() <= 0.0) {
                                        return;
                                    }
                                    if (!this.growthProcessingCooldown.isEmpty() || this.chunkLoadQueue.isEmpty()) {
                                        return;
                                    }
                                    PlantChunkObject polled = this.chunkLoadQueue.poll();
                                    if (polled.getChunk().isChunkLoaded()) {
                                        this.growthProcessingCooldown.put(polled, 0L);
                                        if (polled.handleChunkLoad(Bukkit.getWorld(polled.getChunk().worldName))) {
                                            this.d(polled.getChunk());
                                        }
                                        if (this.pendingUnloadAfterGrowth.contains(polled)) {
                                            polled.handleChunkUnload();
                                            this.pendingUnloadAfterGrowth.remove(polled);
                                        }
                                        this.growthProcessingCooldown.remove(polled);
                                        return;
                                    }
                                    if (this.pendingUnloadAfterGrowth.contains(polled)) {
                                        this.pendingUnloadAfterGrowth.remove(polled);
                                    }
                                },
                                1L,
                                1L);
    }

    public final void a() {
        for (PlantChunkObject plantChunkObject : this.plantDatabase.loadObjects()) {
            if (this.plugin.f().isWorldBlacklisted(plantChunkObject.getChunk().worldName)) continue;
            this.chunkObjectsByKey.put(plantChunkObject.getChunk(), plantChunkObject);
        }
        this.plugin.getLogger()
                .log(Level.INFO, "Loaded " + this.chunkObjectsByKey.size() + " chunks into memory on first startup");
    }

    public final void a(ChunkKey chunkKey) {
        if (this.plantDatabase.objectExists(chunkKey.toString())) {
            this.chunkObjectsByKey.put(chunkKey, this.plantDatabase.loadObject(chunkKey.toString()));
        }
        if (chunkKey.isChunkLoaded()) {
            this.c(chunkKey);
        }
    }

    public final void b() {
        Collections.unmodifiableCollection(this.chunkObjectsByKey.values()).stream()
                .filter(PlantChunkObject::hasChangedSinceLatestSave)
                .forEach(
                        plantChunkObject -> {
                            this.plantDatabase.saveObjectAsync(plantChunkObject);
                            plantChunkObject.setHasChangedSinceLatestSave(false);
                        });
    }

    public final void c() {
        for (ChunkKey chunkKey : this.chunkObjectsByKey.keySet()) {
            if (!chunkKey.isChunkLoaded()) continue;
            this.chunkObjectsByKey.get(chunkKey).setLastUnloaded(System.currentTimeMillis());
        }
        Collections.unmodifiableCollection(this.chunkObjectsByKey.values()).stream()
                .filter(PlantChunkObject::hasChangedSinceLatestSave)
                .forEach(
                        plantChunkObject -> {
                            this.plantDatabase.saveObject(plantChunkObject);
                            plantChunkObject.setHasChangedSinceLatestSave(false);
                        });
        for (PlantChunkObject plantChunkObject2 : this.chunksMarkedForSqlDelete) {
            this.plantDatabase.deleteObject(plantChunkObject2);
        }
        this.chunksMarkedForSqlDelete.clear();
        this.chunkObjectsByKey.clear();
        this.plantDatabase.close();
    }

    public final PlantChunkObject b(ChunkKey chunkKey) {
        return this.chunkObjectsByKey.getOrDefault(chunkKey, null);
    }

    public final void c(ChunkKey chunkKey) {
        PlantChunkObject chunkObj = this.b(chunkKey);
        if (chunkObj == null) {
            return;
        }
        if (System.currentTimeMillis() - chunkObj.getLastLoaded() < 10000L) {
            return;
        }
        this.b(chunkObj);
    }

    public void runLoadedTrackedCropGrowth(long deltaWallMs, double multiplier) {
        if (multiplier <= 0.0 || deltaWallMs <= 0L) {
            return;
        }
        for (PlantChunkObject pco : new HashSet<>(this.chunkObjectsByKey.values())) {
            ChunkKey ck = pco.getChunk();
            if (!ck.isChunkLoaded()) {
                continue;
            }
            Set<PlantBlock> locs = pco.getPlantLocs();
            if (locs == null || locs.isEmpty()) {
                continue;
            }
            World w = Bukkit.getWorld(ck.worldName);
            if (w == null || this.plugin.f().isWorldBlacklisted(w.getName())) {
                continue;
            }
            pco.tickLoadedTrackedCrops(w, deltaWallMs, multiplier);
        }
    }

    public void runLoadedPassiveMobGrowth(long deltaWallMs, double multiplier) {
        if (multiplier <= 0.0 || deltaWallMs <= 0L || this.plugin.getMobGrowthRegistry() == null) {
            return;
        }
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds) {
            if (this.plugin.f().isWorldBlacklisted(world.getName())) {
                continue;
            }
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    EntityGrowthProcessor.tickLoadedPassiveMobs(entity, deltaWallMs, multiplier);
                }
            }
        }
    }

    public void scanChunkForTrackedCropMaterials(Chunk chunk, Set<Material> registeredMaterials) {
        if (chunk == null || !chunk.isLoaded() || registeredMaterials == null || registeredMaterials.isEmpty()) {
            return;
        }
        World world = chunk.getWorld();
        if (this.plugin.f().isWorldBlacklisted(world.getName())) {
            return;
        }
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (!registeredMaterials.contains(block.getType())) {
                        continue;
                    }
                    Location loc = block.getLocation();
                    if (!KeepMeGrowingWorldGuard.allowsGrowth(loc)) {
                        continue;
                    }
                    this.a(loc);
                }
            }
        }
    }

    public final void a(Location location) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(location.getWorld().getName())) {
            return;
        }
        ChunkKey key = ChunkKey.fromChunk(location.getChunk());
        PlantChunkObject plantChunkObject = this.b(key);
        if (plantChunkObject == null) {
            plantChunkObject = new PlantChunkObject(key);
            this.putOrUpdateChunk(plantChunkObject);
        }
        plantChunkObject.addLocation(PlantBlock.fromLocation(location));
    }

    public final void b(Location location) {
        ChunkKey key = ChunkKey.fromChunk(location.getChunk());
        PlantChunkObject plantChunkObject = this.b(key);
        if (plantChunkObject != null && plantChunkObject.removeLocation(PlantBlock.fromLocation(location))) {
            this.d(key);
        }
    }

    private void putOrUpdateChunk(PlantChunkObject plantChunkObject) {
        this.chunkObjectsByKey.put(plantChunkObject.getChunk(), plantChunkObject);
        this.chunksMarkedForSqlDelete.remove(plantChunkObject);
    }

    public final void d(ChunkKey chunkKey) {
        this.chunksMarkedForSqlDelete.add(this.chunkObjectsByKey.get(chunkKey));
        this.chunkObjectsByKey.remove(chunkKey);
    }

    private void b(PlantChunkObject plantChunkObject) {
        if (!this.chunkLoadQueue.contains(plantChunkObject)
                && !this.growthProcessingCooldown.containsKey(plantChunkObject)) {
            this.chunkLoadQueue.add(plantChunkObject);
        }
    }
}
