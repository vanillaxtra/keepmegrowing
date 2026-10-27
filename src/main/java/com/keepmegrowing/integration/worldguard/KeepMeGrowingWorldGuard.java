package com.keepmegrowing.integration.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


public final class KeepMeGrowingWorldGuard {

    private static final String HOOK_CLASS =
            "com.keepmegrowing.integration.worldguard.WorldGuardHook";

    private static boolean loaded;
    private static WorldGuardBridge hook;

    private KeepMeGrowingWorldGuard() {}

    private static WorldGuardBridge hook() {
        if (loaded) {
            return hook;
        }
        loaded = true;
        Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wg == null) {
            return null;
        }
        try {
            hook = (WorldGuardBridge) Class.forName(HOOK_CLASS).getDeclaredConstructor().newInstance();
        } catch (Throwable ignored) {
            hook = null;
        }
        return hook;
    }

    public static void registerFlags() {
        WorldGuardBridge bridge = hook();
        if (bridge != null) {
            bridge.registerFlags();
        }
    }

    public static boolean allowsGrowth(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        WorldGuardBridge bridge = hook();
        if (bridge == null) {
            return true;
        }
        try {
            return bridge.allowsGrowth(location);
        } catch (Throwable ignored) {
            return true;
        }
    }
}
