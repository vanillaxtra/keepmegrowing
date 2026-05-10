
package com.keepmegrowing.databaseapi.database;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class Database<T> {
    private AbstractDatabaseHandler<T> handler;
    private Logger logger;
    private DatabaseSettings settings;

    public Database(DatabaseSettings databaseSettings, Class<T> clazz) {
        this.logger = databaseSettings.getPlugin().getLogger();
        DatabaseSetup databaseSetup = DatabaseSetup.getDatabase(databaseSettings);
        this.handler = databaseSetup.getHandler(clazz, databaseSettings);
        this.settings = databaseSettings;
    }

    public @NonNull List<T> loadObjects() {
        List<T> list = new ArrayList<>();
        try {
            list = this.handler.loadObjects();
        }
        catch (Exception exception) {
            this.logger.severe(() -> "Could not load objects from database! Error: " + exception.getMessage());
        }
        return list;
    }

    public @Nullable T loadObject(String string) {
        T t2 = null;
        try {
            t2 = this.handler.loadObject(string);
        }
        catch (Exception exception) {
            this.logger.severe(() -> "Could not load object from database! " + exception.getMessage());
        }
        return t2;
    }

    public CompletableFuture<Boolean> saveObjectAsync(T t2) {
        try {
            return this.handler.saveObject(t2);
        }
        catch (Exception exception) {
            this.logger.severe(() -> "Could not save object to database! Error: " + exception.getMessage());
            return new CompletableFuture<Boolean>();
        }
    }

    @Deprecated
    public boolean saveObject(T t2) {
        this.saveObjectAsync(t2).thenAccept(bl -> {
            if (Boolean.FALSE.equals(bl)) {
                this.logger.severe(() -> "Could not save object to database!");
            }
        });
        return true;
    }

    public boolean objectExists(String string) {
        return this.handler.objectExists(string);
    }

    public void deleteID(String string) {
        this.handler.deleteID(string);
    }

    public void deleteObject(T t2) {
        try {
            this.handler.deleteObject(t2);
            return;
        }
        catch (Exception exception) {
            this.logger.severe(() -> "Could not delete object! Error: " + exception.getMessage());
            return;
        }
    }

    public void close() {
        this.handler.close();
    }
}
