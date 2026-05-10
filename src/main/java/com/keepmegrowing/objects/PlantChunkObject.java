
package com.keepmegrowing.objects;

import com.google.gson.annotations.Expose;
import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.databaseapi.database.objects.DataObject;
import com.keepmegrowing.objects.ChunkKey;
import com.keepmegrowing.objects.PlantBlock;
import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.CropGrowthRegistry;
import com.keepmegrowing.integration.worldguard.KeepMeGrowingWorldGuard;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.World;

public class PlantChunkObject
implements DataObject {
    @Expose
    private String uniqueId;
    @Expose
    private ChunkKey chunk;
    @Expose
    private long lastLoaded;
    @Expose
    private long lastUnloaded;
    @Expose
    Set<PlantBlock> plantLocs;
    private boolean hasChangedSinceLatestSave = false;
    static CropGrowthRegistry cropRegistry = KeepMeGrowing.a().c();

    public PlantChunkObject() {
    }

    public PlantChunkObject(ChunkKey chunkKey) {
        this.chunk = chunkKey;
        this.plantLocs = new HashSet<PlantBlock>();
        this.lastUnloaded = -1L;
        this.lastLoaded = System.currentTimeMillis();
        this.uniqueId = chunkKey.toString();
        this.hasChangedSinceLatestSave = true;
    }

    public long getOfflineGrowthAnchorTimeMs() {
        return lastUnloaded != -1L ? lastUnloaded : lastLoaded;
    }

    public boolean handleChunkLoad(World world) {
        this.lastLoaded = System.currentTimeMillis();
        this.hasChangedSinceLatestSave = true;
        long anchor = getOfflineGrowthAnchorTimeMs();
        Iterator<PlantBlock> iterator = this.plantLocs.iterator();
        while (iterator.hasNext()) {
            PlantBlock plantPos = iterator.next();
            if (!KeepMeGrowingWorldGuard.allowsGrowth(plantPos.getBlock(world).getLocation())) {
                continue;
            }
            Material blockType = plantPos.getBlock(world).getType();
            AbstractCropGrowth growth = cropRegistry.findGrowth(blockType);
            if (growth == null) {
                iterator.remove();
                continue;
            }
            KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
            double mult = plugin != null ? plugin.getGrowthRateController().getEffectiveMultiplier() : 1.0;
            if (!growth.a(plantPos.getBlock(world).getState(), anchor, mult)) continue;
            iterator.remove();
        }
        return this.plantLocs.isEmpty();
    }

    public void tickLoadedTrackedCrops(World world, long deltaWallMs, double multiplier) {
        if (deltaWallMs <= 0L || multiplier <= 0.0 || plantLocs == null || plantLocs.isEmpty()) {
            return;
        }
        long syntheticOfflineStart = System.currentTimeMillis() - deltaWallMs;
        Iterator<PlantBlock> iterator = plantLocs.iterator();
        boolean touched = false;
        while (iterator.hasNext()) {
            PlantBlock plantPos = iterator.next();
            if (!KeepMeGrowingWorldGuard.allowsGrowth(plantPos.getBlock(world).getLocation())) {
                continue;
            }
            Material blockType = plantPos.getBlock(world).getType();
            AbstractCropGrowth growth = cropRegistry.findGrowth(blockType);
            if (growth == null) {
                iterator.remove();
                touched = true;
                continue;
            }
            if (growth.a(plantPos.getBlock(world).getState(), syntheticOfflineStart, multiplier)) {
                iterator.remove();
                touched = true;
            }
        }
        if (touched) {
            hasChangedSinceLatestSave = true;
        }
    }

    public void handleChunkUnload() {
        this.lastUnloaded = System.currentTimeMillis();
        this.hasChangedSinceLatestSave = true;
    }

    public void addLocation(PlantBlock b2) {
        if (this.plantLocs.add(b2)) {
            this.hasChangedSinceLatestSave = true;
        }
    }

    public boolean removeLocation(PlantBlock b2) {
        if (this.plantLocs.remove(b2)) {
            this.hasChangedSinceLatestSave = true;
        }
        return this.plantLocs.isEmpty();
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public void setUniqueId(String string) {
        this.uniqueId = string;
    }

    public ChunkKey getChunk() {
        return this.chunk;
    }

    public long getLastUnloaded() {
        return this.lastUnloaded;
    }

    public void setLastUnloaded(long l) {
        this.lastUnloaded = l;
        this.hasChangedSinceLatestSave = true;
    }

    public long getLastLoaded() {
        return this.lastLoaded;
    }

    public Set<PlantBlock> getPlantLocs() {
        return this.plantLocs;
    }

    public boolean hasChangedSinceLatestSave() {
        return this.hasChangedSinceLatestSave;
    }

    public void setHasChangedSinceLatestSave(boolean bl) {
        this.hasChangedSinceLatestSave = bl;
    }

    public int hashCode() {
        return this.chunk.hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (PlantChunkObject)object;
        return this.getChunk().equals(((PlantChunkObject)object).getChunk());
    }
}
