package com.keepmegrowing.integration.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.Location;


public final class KeepMeGrowingWorldGuard {

    private static boolean worldGuardChecked;
    private static boolean worldGuardPresent;

    private KeepMeGrowingWorldGuard() {}

    private static boolean isWorldGuardPresent() {
        if (!worldGuardChecked) {
            worldGuardPresent = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
            worldGuardChecked = true;
        }
        return worldGuardPresent;
    }

    public static void registerFlags() {
        if (isWorldGuardPresent()) {
            WorldGuardHook.registerFlags();
        }
    }

    public static boolean allowsGrowth(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        if (!isWorldGuardPresent()) {
            return true;
        }
        return WorldGuardHook.allowsGrowth(location);
    }
}
