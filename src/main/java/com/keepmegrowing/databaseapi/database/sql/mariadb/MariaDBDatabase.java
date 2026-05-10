
package com.keepmegrowing.databaseapi.database.sql.mariadb;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.sql.mariadb.MariaDBDatabaseConnector;
import com.keepmegrowing.databaseapi.database.sql.mariadb.MariaDBDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class MariaDBDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        JavaPlugin javaPlugin = databaseSettings.getPlugin();
        MariaDBDatabaseConnector mariaDBDatabaseConnector = new MariaDBDatabaseConnector(databaseSettings);
        return new MariaDBDatabaseHandler<T>(javaPlugin, clazz, mariaDBDatabaseConnector, databaseSettings);
    }
}
