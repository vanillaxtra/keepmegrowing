
package com.keepmegrowing.databaseapi.database;

import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public abstract class AbstractDatabaseHandler<T> {
    protected Queue<Runnable> processQueue;
    private BukkitTask asyncSaveTask;
    private boolean inSave;
    protected boolean shutdown;
    protected static final String DATABASE_FOLDER_NAME = "database";
    protected Class<T> dataObject;
    protected DatabaseConnector databaseConnector;
    protected JavaPlugin plugin;
    protected DatabaseSettings settings;

    protected AbstractDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        this.plugin = javaPlugin;
        this.databaseConnector = databaseConnector;
        this.dataObject = clazz;
        this.settings = databaseSettings;
        if (!javaPlugin.isEnabled()) {
            return;
        }
        this.processQueue = new ConcurrentLinkedQueue<Runnable>();
        this.asyncSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)javaPlugin, () -> {
            if (this.shutdown || !javaPlugin.isEnabled()) {
                databaseConnector.closeConnection(this.dataObject);
                this.asyncSaveTask.cancel();
                return;
            }
            if (!this.inSave && !this.processQueue.isEmpty()) {
                this.inSave = true;
                while (!this.processQueue.isEmpty()) {
                    this.processQueue.poll().run();
                }
                this.inSave = false;
            }
        }, 0L, 1L);
    }

    protected AbstractDatabaseHandler() {
    }

    public abstract List<T> loadObjects();

    public abstract @Nullable T loadObject(@NonNull String var1);

    public abstract CompletableFuture<Boolean> saveObject(T var1);

    public abstract void deleteObject(T var1);

    public abstract boolean objectExists(String var1);

    public abstract void close();

    public abstract void deleteID(String var1);
}
