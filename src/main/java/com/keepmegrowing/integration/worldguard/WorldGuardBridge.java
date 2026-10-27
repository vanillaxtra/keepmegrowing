package com.keepmegrowing.integration.worldguard;

import org.bukkit.Location;

public interface WorldGuardBridge {
    void registerFlags();

    boolean allowsGrowth(Location location);
}
