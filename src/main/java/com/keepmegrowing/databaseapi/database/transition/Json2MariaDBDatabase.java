
package com.keepmegrowing.databaseapi.database.transition;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.json.JSONDatabase;
import com.keepmegrowing.databaseapi.database.sql.mariadb.MariaDBDatabase;
import com.keepmegrowing.databaseapi.database.transition.TransitionDatabaseHandler;

public class Json2MariaDBDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        return new TransitionDatabaseHandler<T>(clazz, new JSONDatabase().getHandler(clazz, databaseSettings), new MariaDBDatabase().getHandler(clazz, databaseSettings));
    }
}
