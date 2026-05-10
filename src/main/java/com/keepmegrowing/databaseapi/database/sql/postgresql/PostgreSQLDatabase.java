
package com.keepmegrowing.databaseapi.database.sql.postgresql;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.sql.postgresql.PostgreSQLDatabaseConnector;
import com.keepmegrowing.databaseapi.database.sql.postgresql.PostgreSQLDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class PostgreSQLDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        JavaPlugin javaPlugin = databaseSettings.getPlugin();
        PostgreSQLDatabaseConnector postgreSQLDatabaseConnector = new PostgreSQLDatabaseConnector(databaseSettings);
        return new PostgreSQLDatabaseHandler<T>(javaPlugin, clazz, postgreSQLDatabaseConnector, databaseSettings);
    }
}
