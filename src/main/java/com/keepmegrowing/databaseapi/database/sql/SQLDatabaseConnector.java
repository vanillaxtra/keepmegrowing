
package com.keepmegrowing.databaseapi.database.sql;

import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.eclipse.jdt.annotation.NonNull;

public abstract class SQLDatabaseConnector
implements DatabaseConnector {
    protected String connectionUrl;
    private DatabaseSettings dbSettings;
    protected static Connection connection = null;
    protected static Set<Class<?>> types = new HashSet();

    public SQLDatabaseConnector(DatabaseSettings databaseSettings, String string) {
        this.dbSettings = databaseSettings;
        this.connectionUrl = string;
    }

    @Override
    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    @Override
    public @NonNull String getUniqueId(String string) {
        return "";
    }

    @Override
    public boolean uniqueIdExists(String string, String string2) {
        return false;
    }

    @Override
    public void closeConnection(Class<?> clazz) {
        types.remove(clazz);
        if (types.isEmpty() && connection != null) {
            try {
                connection.close();
                Bukkit.getLogger().info("Closed database connection");
                return;
            }
            catch (SQLException sQLException) {
                Bukkit.getLogger().severe("Could not close database connection");
            }
        }
    }

    @Override
    public Object createConnection(Class<?> clazz) {
        types.add(clazz);
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(this.connectionUrl, this.dbSettings.getDatabaseUsername(), this.dbSettings.getDatabasePassword());
            }
            catch (SQLException sQLException) {
                Bukkit.getLogger().severe("Could not connect to the database! " + sQLException.getMessage());
            }
        }
        return connection;
    }
}
