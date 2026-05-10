
package com.keepmegrowing.database;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.database.KeepMeGrowingTypeAdapterFactory;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.TypeAdapterFactories;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import org.bukkit.configuration.file.FileConfiguration;

public class DBSettings {
    private int backupPeriod = 5;
    DatabaseSettings settings;

    public void setup() {
        FileConfiguration cfg = KeepMeGrowing.a().getConfig();
        DatabaseSetup.DatabaseType databaseType = DatabaseSetup.DatabaseType.valueOf(cfg.getString("database.type", "JSON"));
        String string = cfg.getString("database.host", "localhost");
        int n = cfg.getInt("database.port", 3306);
        String string2 = cfg.getString("database.name", "offline_growth");
        String string3 = cfg.getString("database.username", "username");
        String string4 = cfg.getString("database.password", "password");
        this.backupPeriod = cfg.getInt("database.backup-period", 5);
        boolean bl = cfg.getBoolean("database.use-ssl", false);
        String string5 = cfg.getString("prefix-character", "");
        String mongoUri = cfg.getString("database.mongodb-connection-uri", "");
        this.settings = new DatabaseSettings.DatabaseSettingsBuilder(KeepMeGrowing.a()).databaseHost(string).databaseType(databaseType).databaseName(string2).databasePort(n).databasePassword(string4).databaseUsername(string3).databasePrefix(string5).useSSL(bl).mongodbConnectionUri(mongoUri).build();
        TypeAdapterFactories.addTypeAdapterFactory(new KeepMeGrowingTypeAdapterFactory());
    }

    public int getBackupPeriod() {
        return this.backupPeriod;
    }

    public DatabaseSettings getSettings() {
        return this.settings;
    }
}
