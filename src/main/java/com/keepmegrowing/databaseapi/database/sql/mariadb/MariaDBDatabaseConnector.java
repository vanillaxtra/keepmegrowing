
package com.keepmegrowing.databaseapi.database.sql.mariadb;

import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseConnector;

public class MariaDBDatabaseConnector
extends SQLDatabaseConnector {
    MariaDBDatabaseConnector(DatabaseSettings databaseSettings) {
        super(databaseSettings, "jdbc:mysql://" + databaseSettings.getDatabaseHost() + ":" + databaseSettings.getDatabasePort() + "/" + databaseSettings.getDatabaseName() + "?autoReconnect=true&useSSL=" + databaseSettings.isUseSSL() + "&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
    }
}
