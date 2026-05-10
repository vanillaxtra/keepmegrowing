
package com.keepmegrowing.databaseapi.database.sql.mariadb;

import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.sql.SQLConfiguration;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class MariaDBDatabaseHandler<T>
extends SQLDatabaseHandler<T> {
    MariaDBDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings, new SQLConfiguration(javaPlugin, clazz, databaseSettings).schema("CREATE TABLE IF NOT EXISTS `[tableName]` (json JSON, uniqueId VARCHAR(255) GENERATED ALWAYS AS (JSON_EXTRACT(json, \"$.uniqueId\")), UNIQUE INDEX i (uniqueId))"));
    }
}
