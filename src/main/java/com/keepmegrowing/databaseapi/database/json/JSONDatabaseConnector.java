
package com.keepmegrowing.databaseapi.database.json;

import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import java.io.File;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.NonNull;

public class JSONDatabaseConnector
implements DatabaseConnector {
    private static final int MAX_LOOPS = 100;
    private static final String DATABASE_FOLDER_NAME = "database";
    private static final String JSON = ".json";
    private final File dataFolder;

    JSONDatabaseConnector(JavaPlugin javaPlugin) {
        this.dataFolder = new File(javaPlugin.getDataFolder(), DATABASE_FOLDER_NAME);
    }

    @Override
    public @NonNull String getUniqueId(String string) {
        UUID uUID = UUID.randomUUID();
        File file = new File(this.dataFolder, string + File.separator + uUID.toString() + JSON);
        int n = 0;
        while (file.exists() && n++ < 100) {
            uUID = UUID.randomUUID();
            file = new File(this.dataFolder, string + File.separator + uUID.toString() + JSON);
        }
        return uUID.toString();
    }

    @Override
    public boolean uniqueIdExists(String folder, String string) {
        File file = new File(this.dataFolder, folder + File.separator + string + JSON);
        return file.exists();
    }

    @Override
    public String getConnectionUrl() {
        return null;
    }

    @Override
    public Object createConnection(Class<?> clazz) {
        return null;
    }

    @Override
    public void closeConnection(Class<?> clazz) {
    }
}
