
package com.keepmegrowing.databaseapi.database.mongodb;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.mongodb.MongoDBDatabaseConnector;
import com.keepmegrowing.databaseapi.database.mongodb.MongoDBDatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MongoDBDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        JavaPlugin javaPlugin = databaseSettings.getPlugin();
        if (Bukkit.getPluginManager().getPlugin("BsbMongo") == null) {
            DatabaseLogger.logError(databaseSettings.getPlugin(), "You must install BsbMongo plugin for MongoDB support!");
            DatabaseLogger.logError(databaseSettings.getPlugin(), "See: https://github.com/tastybento/bsbMongo/releases/");
            Bukkit.getPluginManager().disablePlugin((Plugin)javaPlugin);
            return null;
        }
        MongoDBDatabaseConnector mongoDBDatabaseConnector = new MongoDBDatabaseConnector(databaseSettings);
        return new MongoDBDatabaseHandler<T>(javaPlugin, clazz, mongoDBDatabaseConnector, databaseSettings);
    }
}
