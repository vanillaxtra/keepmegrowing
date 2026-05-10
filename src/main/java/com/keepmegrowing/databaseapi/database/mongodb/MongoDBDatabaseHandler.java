
package com.keepmegrowing.databaseapi.database.mongodb;

import com.google.gson.Gson;
import com.mongodb.MongoClientException;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.AbstractJSONDatabaseHandler;
import com.keepmegrowing.databaseapi.database.objects.DataObject;
import com.keepmegrowing.databaseapi.database.objects.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MongoDBDatabaseHandler<T>
extends AbstractJSONDatabaseHandler<T> {
    private static final String UNIQUEID = "uniqueId";
    private static final String MONGO_ID = "_id";
    private MongoCollection<Document> collection;
    private DatabaseConnector dbConnecter;

    MongoDBDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings);
        this.dbConnecter = databaseConnector;
        boolean ok = true;
        try {
            MongoDatabase mongoDatabase = (MongoDatabase) databaseConnector.createConnection(this.dataObject);
            if (mongoDatabase == null) {
                DatabaseLogger.logError(
                        databaseSettings.getPlugin(),
                        "Could not connect to the database. Are the credentials in the config.yml file correct?");
                ok = false;
            } else {
                String legacyName = databaseSettings.getDatabasePrefix() + this.dataObject.getCanonicalName();
                String collectionName = this.getName(javaPlugin, this.dataObject);
                if (!legacyName.equals(collectionName)
                        && this.collectionExists(mongoDatabase, legacyName)
                        && !this.collectionExists(mongoDatabase, collectionName)) {
                    this.collection = mongoDatabase.getCollection(legacyName);
                    this.collection.renameCollection(new MongoNamespace(mongoDatabase.getName(), collectionName));
                } else {
                    this.collection = mongoDatabase.getCollection(collectionName);
                }
                IndexOptions indexOptions = new IndexOptions().unique(true);
                this.collection.createIndex(Indexes.ascending(UNIQUEID), indexOptions);
            }
        } catch (MongoTimeoutException mongoTimeoutException) {
            DatabaseLogger.logError(databaseSettings.getPlugin(), "Could not connect to the database. MongoDB timed out.");
            DatabaseLogger.logError(databaseSettings.getPlugin(), "Error code: " + mongoTimeoutException.getCode());
            ok = false;
        } catch (MongoClientException mongoClientException) {
            DatabaseLogger.logError(databaseSettings.getPlugin(), "Could not connect to the database. An unhandled error occurred.");
            DatabaseLogger.logStacktrace((Exception) ((Object) mongoClientException));
            ok = false;
        }
        if (!ok) {
            DatabaseLogger.logError(databaseSettings.getPlugin(), "Disabling JavaPlugin...");
            Bukkit.getPluginManager().disablePlugin((Plugin) javaPlugin);
        }
    }

    private boolean collectionExists(MongoDatabase mongoDatabase, String string) {
        for (String string2 : mongoDatabase.listCollectionNames()) {
            if (!string2.equalsIgnoreCase(string)) continue;
            return true;
        }
        return false;
    }

    private String getName(JavaPlugin javaPlugin, Class<T> clazz) {
        return this.settings.getDatabasePrefix() + (clazz.getAnnotation(Table.class) == null ? clazz.getCanonicalName() : clazz.getAnnotation(Table.class).name());
    }

    @Override
    public List<T> loadObjects() {
        ArrayList<T> results = new ArrayList<>();
        Gson gson = this.getGson();
        for (Document doc : this.collection.find(new Document())) {
            String json = doc.toJson().replaceFirst(MONGO_ID, UNIQUEID);
            try {
                results.add(gson.fromJson(json, this.dataObject));
            }
            catch (Exception exception) {
                DatabaseLogger.logError(this.settings.getPlugin(), "Could not load object :" + exception.getMessage());
            }
        }
        return results;
    }

    @Override
    public T loadObject(String string) {
        Document doc = this.collection.find((Bson) new Document(MONGO_ID, string)).limit(1).first();
        if (doc == null) {
            return null;
        }
        Gson gson = this.getGson();
        String json = doc.toJson().replaceFirst(MONGO_ID, UNIQUEID);
        return gson.fromJson(json, this.dataObject);
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T t2) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        if (t2 == null) {
            DatabaseLogger.logError(this.settings.getPlugin(), "MongoDB database request to store a null. ");
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.settings.getPlugin(), "This class is not a DataObject: " + t2.getClass().getName());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        DataObject dataObject = (DataObject) t2;
        try {
            Gson gson = this.getGson();
            String json = gson.toJson(t2);
            json = json.replaceFirst(UNIQUEID, MONGO_ID);
            Document document = Document.parse(json);
            Bson filter = new Document(MONGO_ID, dataObject.getUniqueId());
            FindOneAndReplaceOptions findOneAndReplaceOptions = new FindOneAndReplaceOptions().upsert(true);
            this.collection.findOneAndReplace(filter, document, findOneAndReplaceOptions);
            completableFuture.complete(Boolean.TRUE);
        } catch (Exception exception) {
            DatabaseLogger.logError(
                    this.settings.getPlugin(),
                    "Could not save object " + t2.getClass().getName() + " " + exception.getMessage());
            completableFuture.complete(Boolean.FALSE);
        }
        return completableFuture;
    }

    @Override
    public void deleteID(String string) {
        try {
            this.collection.findOneAndDelete((Bson)new Document(MONGO_ID, (Object)string));
            return;
        }
        catch (Exception exception) {
            MongoDBDatabaseHandler mongoDBDatabaseHandler = this;
            DatabaseLogger.logError(this.settings.getPlugin(), "Could not delete object " + mongoDBDatabaseHandler.getName(mongoDBDatabaseHandler.plugin, this.dataObject) + " " + string + " " + exception.getMessage());
            return;
        }
    }

    @Override
    public void deleteObject(T t2) {
        if (t2 == null) {
            DatabaseLogger.logError(this.settings.getPlugin(), "MondDB database request to delete a null. ");
            return;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.settings.getPlugin(), "This class is not a DataObject: " + t2.getClass().getName());
            return;
        }
        ((AbstractDatabaseHandler)this).deleteID(((DataObject)t2).getUniqueId());
    }

    @Override
    public boolean objectExists(String string) {
        return this.collection.find((Bson)new Document(MONGO_ID, (Object)string)).first() != null;
    }

    @Override
    public void close() {
        this.dbConnecter.closeConnection(this.dataObject);
    }
}
