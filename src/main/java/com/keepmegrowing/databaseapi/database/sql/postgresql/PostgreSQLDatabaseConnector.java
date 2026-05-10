
package com.keepmegrowing.databaseapi.database.sql.postgresql;

import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseConnector;
import org.eclipse.jdt.annotation.NonNull;
import org.postgresql.Driver;

public class PostgreSQLDatabaseConnector
extends SQLDatabaseConnector {
    PostgreSQLDatabaseConnector(@NonNull DatabaseSettings databaseSettings) {
        super(databaseSettings, "jdbc:postgresql://" + databaseSettings.getDatabaseHost() + ":" + databaseSettings.getDatabasePort() + "/" + databaseSettings.getDatabaseName() + "?autoReconnect=true&useSSL=" + databaseSettings.isUseSSL() + "&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
    }

    static {
        new Driver();
    }
}
