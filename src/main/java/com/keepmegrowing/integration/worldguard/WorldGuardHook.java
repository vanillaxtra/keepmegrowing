package com.keepmegrowing.integration.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;


final class WorldGuardHook {
    static final StateFlag OFFLINE_GROWTH = new StateFlag("offline-growth", true);

    private static RegionQuery regionQuery;

    private WorldGuardHook() {}

    static void registerFlags() {
        try {
            WorldGuard.getInstance().getFlagRegistry().register(OFFLINE_GROWTH);
        } catch (FlagConflictException ignored) {
        } catch (Throwable ignored) {
        }
    }

    static boolean allowsGrowth(Location location) {
        try {
            if (regionQuery == null) {
                regionQuery =
                        WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            }
            return regionQuery.testState(BukkitAdapter.adapt(location), null, OFFLINE_GROWTH);
        } catch (RuntimeException e) {
            return true;
        }
    }
}
