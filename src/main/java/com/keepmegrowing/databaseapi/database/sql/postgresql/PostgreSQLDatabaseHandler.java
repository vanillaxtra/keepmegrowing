
package com.keepmegrowing.databaseapi.database.sql.postgresql;

import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.objects.DataObject;
import com.keepmegrowing.databaseapi.database.sql.SQLConfiguration;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.java.JavaPlugin;

public class PostgreSQLDatabaseHandler<T>
extends SQLDatabaseHandler<T> {
    PostgreSQLDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings, new SQLConfiguration(javaPlugin, clazz, databaseSettings).schema("CREATE TABLE IF NOT EXISTS \"[tableName]\" (uniqueid VARCHAR PRIMARY KEY, json jsonb NOT NULL)").loadObject("SELECT * FROM \"[tableName]\" WHERE uniqueid = ? LIMIT 1").deleteObject("DELETE FROM \"[tableName]\" WHERE uniqueid = ?").saveObject("INSERT INTO \"[tableName]\" (uniqueid, json) VALUES (?, cast(? as json)) ON CONFLICT (uniqueid) DO UPDATE SET json = cast(? as json)").loadObjects("SELECT json FROM \"[tableName]\"").objectExists("SELECT EXISTS(SELECT * FROM \"[tableName]\" WHERE uniqueid = ?)").renameTable("ALTER TABLE IF EXISTS \"[oldTableName]\" RENAME TO \"[tableName]\"").setUseQuotes(false));
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T t2) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        if (t2 == null) {
            DatabaseLogger.logError(this.plugin, "PostgreSQL database request to store a null. ");
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + t2.getClass().getName());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        com.google.gson.Gson gson = this.getGson();
        String json = gson.toJson(t2);
        String string = ((DataObject)t2).getUniqueId();
        this.processQueue.add(() -> this.persistPostgresSave(string, json, completableFuture, t2));
        return completableFuture;
    }

    private void persistPostgresSave(
            String uniqueId, String json, CompletableFuture<Boolean> completableFuture, T t2) {
        try (PreparedStatement preparedStatement =
                this.getConnection().prepareStatement(this.getSqlConfig().getSaveObjectSQL())) {
            preparedStatement.setString(1, uniqueId);
            preparedStatement.setString(2, json);
            preparedStatement.setString(3, json);
            preparedStatement.execute();
            completableFuture.complete(Boolean.TRUE);
        } catch (SQLException e) {
            DatabaseLogger.logError(
                    this.plugin, "Could not save object " + t2.getClass().getName() + " " + e.getMessage());
            completableFuture.complete(Boolean.FALSE);
        }
    }
}
