package com.keepmegrowing.listener;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.mob.EntityGrowthProcessor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

public final class ChunkEntityLifecycleListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin == null || plugin.getGrowthConfig().isWorldBlacklisted(event.getWorld().getName())) {
            return;
        }
        for (Entity entity : event.getEntities()) {
            EntityGrowthProcessor.applyOfflineGrowth(entity);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin == null || plugin.getGrowthConfig().isWorldBlacklisted(event.getWorld().getName())) {
            return;
        }
        for (Entity entity : event.getEntities()) {
            EntityGrowthProcessor.markEntityUnloaded(entity);
        }
    }
}
