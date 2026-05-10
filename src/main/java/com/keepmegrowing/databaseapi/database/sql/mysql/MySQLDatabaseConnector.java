
package com.keepmegrowing.databaseapi.database.sql.mysql;

import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.sql.SQLDatabaseConnector;

public class MySQLDatabaseConnector
extends SQLDatabaseConnector {
    MySQLDatabaseConnector(DatabaseSettings databaseSettings) {
        super(databaseSettings, "jdbc:mysql://" + databaseSettings.getDatabaseHost() + ":" + databaseSettings.getDatabasePort() + "/" + databaseSettings.getDatabaseName() + "?autoReconnect=true&useSSL=" + databaseSettings.isUseSSL() + "&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
    }
}
