
package com.keepmegrowing.databaseapi.database.sql.sqlite;

import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseConnector;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.NonNull;

public class SQLiteDatabaseConnector
extends SQLDatabaseConnector {
    private static final String DATABASE_FOLDER_NAME = "database";

    SQLiteDatabaseConnector(@NonNull JavaPlugin javaPlugin) {
        super(null, "");
        File file = new File(javaPlugin.getDataFolder(), DATABASE_FOLDER_NAME);
        if (!file.exists() && !file.mkdirs()) {
            DatabaseLogger.logError(javaPlugin, "Could not create database folder!");
            return;
        }
        this.connectionUrl = "jdbc:sqlite:" + file.getAbsolutePath() + File.separator + "database.db";
    }

    @Override
    public Object createConnection(Class<?> clazz) {
        types.add(clazz);
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(this.connectionUrl);
            }
            catch (SQLException sQLException) {
                Bukkit.getLogger().severe("Could not connect to the database! " + sQLException.getMessage());
            }
        }
        return connection;
    }
}
