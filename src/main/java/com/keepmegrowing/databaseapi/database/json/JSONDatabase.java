
package com.keepmegrowing.databaseapi.database.json;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.json.JSONDatabaseConnector;
import com.keepmegrowing.databaseapi.database.json.JSONDatabaseHandler;

public class JSONDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        JSONDatabaseConnector jSONDatabaseConnector = new JSONDatabaseConnector(databaseSettings.getPlugin());
        return new JSONDatabaseHandler<T>(databaseSettings.getPlugin(), clazz, jSONDatabaseConnector, databaseSettings);
    }
}
