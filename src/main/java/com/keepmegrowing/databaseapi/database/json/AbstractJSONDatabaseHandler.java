
package com.keepmegrowing.databaseapi.database.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.TypeAdapterFactories;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractJSONDatabaseHandler<T>
extends AbstractDatabaseHandler<T> {
    private Gson gson;

    protected AbstractJSONDatabaseHandler(JavaPlugin javaPlugin, Class<T> object, DatabaseConnector databaseConnector2, DatabaseSettings databaseSettings) {
        super(javaPlugin, object, databaseConnector2, databaseSettings);
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().enableComplexMapKeySerialization().setPrettyPrinting();
        for (TypeAdapterFactory typeAdapterFactory : TypeAdapterFactories.getTypeAdapterFactories()) {
            gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
        }
        gsonBuilder.disableHtmlEscaping();
        this.gson = gsonBuilder.create();
    }

    protected Gson getGson() {
        return this.gson;
    }
}
