
package com.keepmegrowing.databaseapi.database;

import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseSettings {
    private DatabaseSetup.DatabaseType databaseType;
    private int backupPeriod;
    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    private boolean useSSL;
    private String mongodbConnectionUri;
    private String databasePrefix;
    private JavaPlugin plugin;

    private DatabaseSettings(DatabaseSetup.DatabaseType databaseType, int n, String string, int n2, String string2, String string3, String string4, boolean bl, String string5, String string6, JavaPlugin javaPlugin) {
        this.databaseType = databaseType;
        this.backupPeriod = n;
        this.databaseHost = string;
        this.databasePort = n2;
        this.databaseName = string2;
        this.databaseUsername = string3;
        this.databasePassword = string4;
        this.useSSL = bl;
        this.mongodbConnectionUri = string5;
        this.databasePrefix = string6;
        this.plugin = javaPlugin;
    }

    public int getBackupPeriod() {
        return this.backupPeriod;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public DatabaseSetup.DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    public void setDatabaseType(DatabaseSetup.DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseHost() {
        return this.databaseHost;
    }

    public void setDatabaseHost(String string) {
        this.databaseHost = string;
    }

    public int getDatabasePort() {
        return this.databasePort;
    }

    public boolean isUseSSL() {
        return this.useSSL;
    }

    public void setUseSSL(boolean bl) {
        this.useSSL = bl;
    }

    public void setDatabasePort(int n) {
        this.databasePort = n;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String string) {
        this.databaseName = string;
    }

    public String getDatabaseUsername() {
        return this.databaseUsername;
    }

    public void setDatabaseUsername(String string) {
        this.databaseUsername = string;
    }

    public String getDatabasePassword() {
        return this.databasePassword;
    }

    public void setDatabasePassword(String string) {
        this.databasePassword = string;
    }

    public String getMongodbConnectionUri() {
        return this.mongodbConnectionUri;
    }

    public void setMongodbConnectionUri(String string) {
        this.mongodbConnectionUri = string;
    }

    public String getDatabasePrefix() {
        if (this.databasePrefix == null) {
            this.databasePrefix = "";
        }
        if (this.databasePrefix.isEmpty()) {
            return "";
        }
        return this.databasePrefix.replaceAll("[^a-zA-Z0-9]", "_");
    }

    public void setDatabasePrefix(String string) {
        this.databasePrefix = string;
    }

    public static class DatabaseSettingsBuilder {
        private DatabaseSetup.DatabaseType databaseType = DatabaseSetup.DatabaseType.JSON;
        private int backupPeriod = 5;
        private String databaseHost = "localhost";
        private int databasePort = 3306;
        private String databaseName = "bentobox";
        private String databaseUsername = "username";
        private String databasePassword = "password";
        private boolean useSSL = false;
        private String mongodbConnectionUri = "";
        private String databasePrefix = "";
        private JavaPlugin plugin;

        public DatabaseSettingsBuilder(JavaPlugin javaPlugin) {
            this.plugin = javaPlugin;
        }

        public DatabaseSettingsBuilder backupPeriod(int n) {
            this.backupPeriod = n;
            return this;
        }

        public DatabaseSettingsBuilder databaseType(DatabaseSetup.DatabaseType databaseType) {
            this.databaseType = databaseType;
            return this;
        }

        public DatabaseSettingsBuilder databaseHost(String string) {
            this.databaseHost = string;
            return this;
        }

        public DatabaseSettingsBuilder useSSL(boolean bl) {
            this.useSSL = bl;
            return this;
        }

        public DatabaseSettingsBuilder databasePort(int n) {
            this.databasePort = n;
            return this;
        }

        public DatabaseSettingsBuilder databaseName(String string) {
            this.databaseName = string;
            return this;
        }

        public DatabaseSettingsBuilder databaseUsername(String string) {
            this.databaseUsername = string;
            return this;
        }

        public DatabaseSettingsBuilder databasePassword(String string) {
            this.databasePassword = string;
            return this;
        }

        public DatabaseSettingsBuilder mongodbConnectionUri(String string) {
            this.mongodbConnectionUri = string;
            return this;
        }

        public DatabaseSettingsBuilder databasePrefix(String string) {
            this.databasePrefix = string;
            return this;
        }

        public DatabaseSettings build() {
            return new DatabaseSettings(this.databaseType, this.backupPeriod, this.databaseHost, this.databasePort, this.databaseName, this.databaseUsername, this.databasePassword, this.useSSL, this.mongodbConnectionUri, this.databasePrefix, this.plugin);
        }
    }
}
