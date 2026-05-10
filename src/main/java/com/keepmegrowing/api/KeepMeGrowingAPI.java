
package com.keepmegrowing.api;

import com.keepmegrowing.KeepMeGrowing;
import org.bukkit.Location;

public class KeepMeGrowingAPI {
    public static void addPlant(Location location) {
        KeepMeGrowing.a().b().a(location);
    }

    public static void removePlant(Location location) {
        KeepMeGrowing.a().b().b(location);
    }
}
