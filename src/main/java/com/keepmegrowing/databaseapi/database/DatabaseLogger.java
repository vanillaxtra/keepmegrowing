
package com.keepmegrowing.databaseapi.database;

import com.keepmegrowing.KeepMeGrowing;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseLogger {
    public static void logError(JavaPlugin javaPlugin, String string) {
        javaPlugin.getLogger().log(Level.SEVERE, string);
    }

    public static void logWarning(JavaPlugin javaPlugin, String string) {
        javaPlugin.getLogger().log(Level.WARNING, string);
    }

    public static void logStacktrace(Exception exception) {
        exception.printStackTrace();
    }

    public static void logError(String string) {
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin != null) {
            plugin.getLogger().log(Level.SEVERE, string);
        } else {
            System.out.println("[KeepMeGrowing] ERROR: " + string);
        }
    }

    public static void logWarning(String string) {
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin != null) {
            plugin.getLogger().log(Level.WARNING, string);
        } else {
            System.out.println("[KeepMeGrowing] Warning: " + string);
        }
    }
}
