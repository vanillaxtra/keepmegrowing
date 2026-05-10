
package com.keepmegrowing.databaseapi.database.sql.mysql;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.sql.mysql.MySQLDatabaseConnector;
import com.keepmegrowing.databaseapi.database.sql.mysql.MySQLDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQLDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        JavaPlugin javaPlugin = databaseSettings.getPlugin();
        MySQLDatabaseConnector mySQLDatabaseConnector = new MySQLDatabaseConnector(databaseSettings);
        return new MySQLDatabaseHandler<T>(javaPlugin, clazz, mySQLDatabaseConnector, databaseSettings);
    }
}
