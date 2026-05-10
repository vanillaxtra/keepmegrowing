
package com.keepmegrowing.databaseapi.database.transition;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.json.JSONDatabase;
import com.keepmegrowing.databaseapi.database.sql.postgresql.PostgreSQLDatabase;
import com.keepmegrowing.databaseapi.database.transition.TransitionDatabaseHandler;

public class PostgreSQL2JsonDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        return new TransitionDatabaseHandler<T>(clazz, new PostgreSQLDatabase().getHandler(clazz, databaseSettings), new JSONDatabase().getHandler(clazz, databaseSettings));
    }
}
