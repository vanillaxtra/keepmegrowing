
package com.keepmegrowing.databaseapi.database.sql.sqlite;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.DatabaseSetup;
import com.keepmegrowing.databaseapi.database.sql.sqlite.SQLiteDatabaseConnector;
import com.keepmegrowing.databaseapi.database.sql.sqlite.SQLiteDatabaseHandler;

public class SQLiteDatabase
implements DatabaseSetup {
    @Override
    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> clazz, DatabaseSettings databaseSettings) {
        SQLiteDatabaseConnector sQLiteDatabaseConnector = new SQLiteDatabaseConnector(databaseSettings.getPlugin());
        return new SQLiteDatabaseHandler<T>(databaseSettings.getPlugin(), clazz, sQLiteDatabaseConnector, databaseSettings);
    }
}
