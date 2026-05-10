
package com.keepmegrowing.databaseapi.database.sql.sqlite;

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

public class SQLiteDatabaseHandler<T>
extends SQLDatabaseHandler<T> {
    protected SQLiteDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings, new SQLConfiguration(javaPlugin, clazz, databaseSettings).schema("CREATE TABLE IF NOT EXISTS `[tableName]` (json JSON, uniqueId VARCHAR(255) NOT NULL PRIMARY KEY)").saveObject("INSERT INTO `[tableName]` (json, uniqueId) VALUES (?, ?) ON CONFLICT(uniqueId) DO UPDATE SET json = ?").objectExists("SELECT EXISTS (SELECT 1 FROM `[tableName]` WHERE `uniqueId` = ?)").renameTable("ALTER TABLE `[oldTableName]` RENAME TO `[tableName]`").setUseQuotes(false));
    }

    @Override
    protected void createSchema() {
        if (this.getSqlConfig().renameRequired()) {
            String sql =
                    "SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE type='table' AND name='"
                            + this.getSqlConfig().getOldTableName()
                            + "' COLLATE NOCASE)";
            try (PreparedStatement ps = this.getConnection().prepareStatement(sql)) {
                rename(ps);
            } catch (SQLException e) {
                DatabaseLogger.logError(
                        this.plugin,
                        "Could not check if "
                                + this.getSqlConfig().getOldTableName()
                                + " exists for data object "
                                + this.dataObject.getCanonicalName()
                                + " "
                                + e.getMessage());
            }
        }
        try (PreparedStatement ps = this.getConnection().prepareStatement(this.getSqlConfig().getSchemaSQL())) {
            ps.execute();
        } catch (SQLException e) {
            DatabaseLogger.logError(
                    this.plugin,
                    "Problem trying to create schema for data object "
                            + this.dataObject.getCanonicalName()
                            + " "
                            + e.getMessage());
        }
    }

    private void rename(PreparedStatement existenceStmt) {
        try (java.sql.ResultSet rs = existenceStmt.executeQuery()) {
            if (rs.next() && rs.getBoolean(1)) {
                String renameSql =
                        this.getSqlConfig()
                                .getRenameTableSQL()
                                .replace(
                                        "[oldTableName]",
                                        this.getSqlConfig()
                                                .getOldTableName()
                                                .replace("[tableName]", this.getSqlConfig().getTableName()));
                try (PreparedStatement ps = this.getConnection().prepareStatement(renameSql)) {
                    ps.execute();
                } catch (SQLException e) {
                    DatabaseLogger.logError(
                            this.plugin,
                            "Could not rename "
                                    + this.getSqlConfig().getOldTableName()
                                    + " for data object "
                                    + this.dataObject.getCanonicalName()
                                    + " "
                                    + e.getMessage());
                }
            }
        } catch (Exception e) {
            DatabaseLogger.logError(
                    this.plugin,
                    "Could not check if "
                            + this.getSqlConfig().getOldTableName()
                            + " exists for data object "
                            + this.dataObject.getCanonicalName()
                            + " "
                            + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T t2) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        if (t2 == null) {
            DatabaseLogger.logError(this.plugin, "SQLite database request to store a null. ");
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + t2.getClass().getName());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        String json = this.getGson().toJson(t2);
        this.processQueue.add(() -> this.persistSqliteSave(json, t2, completableFuture));
        return completableFuture;
    }

    private void persistSqliteSave(String json, T t2, CompletableFuture<Boolean> completableFuture) {
        try (PreparedStatement preparedStatement =
                this.getConnection().prepareStatement(this.getSqlConfig().getSaveObjectSQL())) {
            preparedStatement.setString(1, json);
            preparedStatement.setString(2, ((DataObject) t2).getUniqueId());
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
