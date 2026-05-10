
package com.keepmegrowing.listener;

import com.keepmegrowing.KeepMeGrowing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void a(WorldLoadEvent worldLoadEvent) {
        KeepMeGrowing.a().f().applySpawnChunkUnloadForWorld(worldLoadEvent.getWorld());
    }
}
