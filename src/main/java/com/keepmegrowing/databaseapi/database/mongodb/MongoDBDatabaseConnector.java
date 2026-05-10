
package com.keepmegrowing.databaseapi.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.eclipse.jdt.annotation.NonNull;

public class MongoDBDatabaseConnector
implements DatabaseConnector {
    private MongoClient client;
    private final DatabaseSettings dbSettings;
    private final Set<Class<?>> types = new HashSet();

    MongoDBDatabaseConnector(DatabaseSettings databaseSettings) {
        this.dbSettings = databaseSettings;
    }

    public MongoDatabase createConnection(Class<?> dataClass) {
        this.types.add(dataClass);
        if (this.client == null) {
            if (this.dbSettings.getMongodbConnectionUri() == null || this.dbSettings.getMongodbConnectionUri().isEmpty()) {
                MongoCredential cred =
                        MongoCredential.createCredential(
                                this.dbSettings.getDatabaseUsername(),
                                this.dbSettings.getDatabaseName(),
                                this.dbSettings.getDatabasePassword().toCharArray());
                MongoClientOptions mongoClientOptions =
                        MongoClientOptions.builder().sslEnabled(this.dbSettings.isUseSSL()).build();
                this.client =
                        new MongoClient(
                                new ServerAddress(
                                        this.dbSettings.getDatabaseHost(), this.dbSettings.getDatabasePort()),
                                cred,
                                mongoClientOptions);
            } else {
                this.client = new MongoClient(new MongoClientURI(this.dbSettings.getMongodbConnectionUri()));
            }
        }
        return this.client.getDatabase(this.dbSettings.getDatabaseName());
    }

    @Override
    public String getConnectionUrl() {
        return "";
    }

    @Override
    public @NonNull String getUniqueId(String string) {
        return "";
    }

    @Override
    public boolean uniqueIdExists(String string, String string2) {
        return false;
    }

    @Override
    public void closeConnection(Class<?> clazz) {
        this.types.remove(clazz);
        if (this.types.isEmpty() && this.client != null) {
            this.client.close();
            Bukkit.getLogger().info("Closed database connection");
        }
    }
}
