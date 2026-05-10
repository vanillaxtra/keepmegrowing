
package com.keepmegrowing.databaseapi.database.sql;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.AbstractJSONDatabaseHandler;
import com.keepmegrowing.databaseapi.database.objects.DataObject;
import com.keepmegrowing.databaseapi.database.sql.SQLConfiguration;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.NonNull;

public class SQLDatabaseHandler<T>
extends AbstractJSONDatabaseHandler<T> {
    protected static final String COULD_NOT_LOAD_OBJECTS = "Could not load objects ";
    protected static final String COULD_NOT_LOAD_OBJECT = "Could not load object ";
    private Connection connection;
    private SQLConfiguration sqlConfig;

    protected SQLDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings, SQLConfiguration sQLConfiguration) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings);
        this.sqlConfig = sQLConfiguration;
        SQLDatabaseHandler sQLDatabaseHandler = this;
        if (sQLDatabaseHandler.setConnection((Connection)sQLDatabaseHandler.databaseConnector.createConnection(clazz))) {
            this.createSchema();
        }
    }

    public SQLConfiguration getSqlConfig() {
        return this.sqlConfig;
    }

    public void setSqlConfig(SQLConfiguration sQLConfiguration) {
        this.sqlConfig = sQLConfiguration;
    }

    protected void createSchema() {
        if (this.sqlConfig.renameRequired()) {
            String rename = this.sqlConfig.getRenameTableSQL()
                .replace("[oldTableName]", this.sqlConfig.getOldTableName())
                .replace("[tableName]", this.sqlConfig.getTableName());
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(rename)) {
                preparedStatement.execute();
            }
            catch (SQLException ex) {
                DatabaseLogger.logError(this.plugin, "Could not rename " + this.sqlConfig.getOldTableName() + " for data object "
                    + this.dataObject.getCanonicalName() + " " + ex.getMessage());
            }
        }
        try (PreparedStatement ps = this.connection.prepareStatement(this.sqlConfig.getSchemaSQL())) {
            ps.execute();
        }
        catch (SQLException ex) {
            DatabaseLogger.logError(this.plugin, "Problem trying to create schema for data object " + this.dataObject.getCanonicalName() + " " + ex.getMessage());
        }
    }

    @Override
    public List<T> loadObjects() {
        try (Statement statement = this.connection.createStatement()) {
            return this.loadIt(statement);
        }
        catch (SQLException ex) {
            DatabaseLogger.logError(this.plugin, COULD_NOT_LOAD_OBJECTS + ex.getMessage());
            return Collections.emptyList();
        }
    }

    private List<T> loadIt(Statement statement) {
        ArrayList<T> out = new ArrayList<>();
        try (ResultSet rs = statement.executeQuery(this.sqlConfig.getLoadObjectsSQL())) {
            Gson gson = this.getGson();
            while (rs.next()) {
                String json = rs.getString("json");
                if (json == null) continue;
                try {
                    T obj = gson.fromJson(json, this.dataObject);
                    if (obj == null) continue;
                    out.add(obj);
                }
                catch (JsonSyntaxException e) {
                    DatabaseLogger.logError(this.plugin, COULD_NOT_LOAD_OBJECT + e.getMessage());
                    DatabaseLogger.logError(this.plugin, json);
                }
            }
        }
        catch (SQLException ex) {
            DatabaseLogger.logError(this.plugin, COULD_NOT_LOAD_OBJECTS + ex.getMessage());
        }
        return out;
    }

    
    @Override
    public T loadObject(@NonNull String string) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(this.sqlConfig.getLoadObjectSQL());){
            ResultSet resultSet;
            block16: {
                preparedStatement.setString(1, this.sqlConfig.isUseQuotes() ? "\"" + string + "\"" : string);
                try {
                    Object object;
                    resultSet = preparedStatement.executeQuery();
                    try {
                        if (!resultSet.next()) break block16;
                        Gson gson = this.getGson();
                        object = gson.fromJson(resultSet.getString("json"), this.dataObject);
                        if (resultSet == null) return (T)object;
                    }
                    catch (Throwable throwable) {
                        if (resultSet == null) throw throwable;
                        try {
                            resultSet.close();
                            throw throwable;
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                        throw throwable;
                    }
                    resultSet.close();
                    return (T)object;
                }
                catch (Exception exception) {
                    DatabaseLogger.logError(this.plugin, COULD_NOT_LOAD_OBJECT + string + " " + exception.getMessage());
                    return null;
                }
            }
            if (resultSet == null) return null;
            resultSet.close();
            return null;
        }
        catch (SQLException sQLException) {
            DatabaseLogger.logError(this.plugin, COULD_NOT_LOAD_OBJECT + string + " " + sQLException.getMessage());
        }
        return null;
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T t2) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        if (t2 == null) {
            DatabaseLogger.logError(this.plugin, "SQL database request to store a null. ");
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + t2.getClass().getName());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        String string = this.getGson().toJson(t2);
        this.processQueue.add(() -> this.store(completableFuture, t2.getClass().getName(), string, this.sqlConfig.getSaveObjectSQL()));
        return completableFuture;
    }

    private void store(CompletableFuture<Boolean> completableFuture, String clazzName, String json, String sql) {
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, clazzName);
            ps.setString(2, json);
            ps.execute();
            completableFuture.complete(Boolean.TRUE);
        }
        catch (SQLException sQLException) {
            DatabaseLogger.logError(this.plugin, "Could not save object " + clazzName + " " + sQLException.getMessage());
            completableFuture.complete(Boolean.FALSE);
        }
    }

    @Override
    public void deleteID(String string) {
        this.processQueue.add(() -> this.delete(string));
    }

    private void delete(String string) {
        try {
            PreparedStatement preparedStatement;
            block8: {
                block7: {
                    preparedStatement = this.connection.prepareStatement(this.sqlConfig.getDeleteObjectSQL());
                    try {
                        preparedStatement.setString(1, this.sqlConfig.isUseQuotes() ? "\"" + string + "\"" : string);
                        preparedStatement.execute();
                        if (preparedStatement == null) break block7;
                    }
                    catch (Throwable throwable) {
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    break block8;
                }
                return;
            }
            preparedStatement.close();
        }
        catch (Exception exception) {
            DatabaseLogger.logError(this.plugin, "Could not delete object " + this.settings.getDatabasePrefix() + this.dataObject.getCanonicalName() + " " + string + " " + exception.getMessage());
        }
    }

    @Override
    public void deleteObject(T t2) {
        if (t2 == null) {
            DatabaseLogger.logError(this.plugin, "SQL database request to delete a null.");
            return;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + t2.getClass().getName());
            return;
        }
        try {
            Method method = this.dataObject.getMethod("getUniqueId", new Class[0]);
            ((AbstractDatabaseHandler)this).deleteID((String)method.invoke(t2, new Object[0]));
            return;
        }
        catch (Exception exception) {
            DatabaseLogger.logError(this.plugin, "Could not delete object " + t2.getClass().getName() + " " + exception.getMessage());
            return;
        }
    }

    
    @Override
    public boolean objectExists(String string) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(this.sqlConfig.getObjectExistsSQL());){
            preparedStatement.setString(1, this.sqlConfig.isUseQuotes() ? "\"" + string + "\"" : string);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                if (!resultSet.next()) return false;
                boolean bl = resultSet.getBoolean(1);
                return bl;
            }
        }
        catch (SQLException sQLException) {
            DatabaseLogger.logError(this.plugin, "Could not check if key exists in database! " + string + " " + sQLException.getMessage());
        }
        return false;
    }

    @Override
    public void close() {
        this.shutdown = true;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean setConnection(Connection connection) {
        if (connection == null) {
            DatabaseLogger.logError(this.plugin, "Could not connect to the database. Are the credentials in the config.yml file correct?");
            DatabaseLogger.logError(this.plugin, "Disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin((Plugin)this.plugin);
            return false;
        }
        this.connection = connection;
        return true;
    }
}
