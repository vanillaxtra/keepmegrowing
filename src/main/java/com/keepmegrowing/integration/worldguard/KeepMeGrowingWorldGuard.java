package com.keepmegrowing.integration.worldguard;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.lang.reflect.Method;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public final class KeepMeGrowingWorldGuard {

    public static final StateFlag OFFLINE_GROWTH = new StateFlag("offline-growth", true);

    private KeepMeGrowingWorldGuard() {}

    public static void registerFlags() {
        try {
            WorldGuard.getInstance().getFlagRegistry().register(OFFLINE_GROWTH);
        } catch (com.sk89q.worldguard.protection.flags.registry.FlagConflictException ignored) {
            
        } catch (Throwable ignored) {
            
        }
    }

    
    public static boolean allowsGrowth(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wg == null || !wg.isEnabled()) {
            return true;
        }
        WorldGuardPlugin plugin = WorldGuardPlugin.inst();
        if (plugin == null) {
            return true;
        }
        try {
            RegionQuery query =
                    WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            OfflinePlayer dummy = Bukkit.getOfflinePlayer(new UUID(0L, 0L));
            LocalPlayer local = plugin.wrapOfflinePlayer(dummy);
            
            Method testState =
                    query.getClass().getMethod("testState", Location.class, LocalPlayer.class, StateFlag.class);
            return (Boolean) testState.invoke(query, location, local, OFFLINE_GROWTH);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return true;
        }
    }
}
