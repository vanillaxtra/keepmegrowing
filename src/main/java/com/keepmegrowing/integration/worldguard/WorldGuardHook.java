package com.keepmegrowing.integration.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;


public final class WorldGuardHook implements WorldGuardBridge {
    private static final StateFlag OFFLINE_GROWTH = new StateFlag("offline-growth", true);

    private RegionQuery regionQuery;

    public WorldGuardHook() {}

    @Override
    public void registerFlags() {
        try {
            WorldGuard.getInstance().getFlagRegistry().register(OFFLINE_GROWTH);
        } catch (Throwable ignored) {
        }
    }

    @Override
    public boolean allowsGrowth(Location location) {
        try {
            if (regionQuery == null) {
                regionQuery =
                        WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            }
            return regionQuery.testState(BukkitAdapter.adapt(location), null, OFFLINE_GROWTH);
        } catch (Throwable e) {
            return true;
        }
    }
}
