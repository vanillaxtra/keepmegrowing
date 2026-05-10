
package com.keepmegrowing.databaseapi.database;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.JSONDatabase;
import com.keepmegrowing.databaseapi.database.mongodb.MongoDBDatabase;
import com.keepmegrowing.databaseapi.database.sql.mariadb.MariaDBDatabase;
import com.keepmegrowing.databaseapi.database.sql.mysql.MySQLDatabase;
import com.keepmegrowing.databaseapi.database.sql.postgresql.PostgreSQLDatabase;
import com.keepmegrowing.databaseapi.database.sql.sqlite.SQLiteDatabase;
import com.keepmegrowing.databaseapi.database.transition.Json2MariaDBDatabase;
import com.keepmegrowing.databaseapi.database.transition.Json2MongoDBDatabase;
import com.keepmegrowing.databaseapi.database.transition.Json2MySQLDatabase;
import com.keepmegrowing.databaseapi.database.transition.Json2PostgreSQLDatabase;
import com.keepmegrowing.databaseapi.database.transition.Json2SQLiteDatabase;
import com.keepmegrowing.databaseapi.database.transition.MariaDB2JsonDatabase;
import com.keepmegrowing.databaseapi.database.transition.MongoDB2JsonDatabase;
import com.keepmegrowing.databaseapi.database.transition.MySQL2JsonDatabase;
import com.keepmegrowing.databaseapi.database.transition.PostgreSQL2JsonDatabase;
import com.keepmegrowing.databaseapi.database.transition.SQLite2JsonDatabase;
import java.util.Arrays;

public interface DatabaseSetup {
    public static DatabaseSetup getDatabase(DatabaseSettings databaseSettings) {
        return Arrays.stream(DatabaseType.values()).filter(databaseSettings.getDatabaseType()::equals).findFirst().map(databaseType -> databaseType.database).orElse(DatabaseType.JSON.database);
    }

    public <T> AbstractDatabaseHandler<T> getHandler(Class<T> var1, DatabaseSettings var2);

    public static enum DatabaseType {
        JSON(new JSONDatabase()),
        JSON2MYSQL(new Json2MySQLDatabase()),
        JSON2MARIADB(new Json2MariaDBDatabase()),
        JSON2MONGODB(new Json2MongoDBDatabase()),
        JSON2SQLITE(new Json2SQLiteDatabase()),
        JSON2POSTGRESQL(new Json2PostgreSQLDatabase()),
        MYSQL(new MySQLDatabase()),
        MYSQL2JSON(new MySQL2JsonDatabase()),
        MARIADB(new MariaDBDatabase()),
        MARIADB2JSON(new MariaDB2JsonDatabase()),
        MONGODB(new MongoDBDatabase()),
        MONGODB2JSON(new MongoDB2JsonDatabase()),
        SQLITE(new SQLiteDatabase()),
        SQLITE2JSON(new SQLite2JsonDatabase()),
        POSTGRESQL(new PostgreSQLDatabase()),
        POSTGRESQL2JSON(new PostgreSQL2JsonDatabase());

        DatabaseSetup database;

        private DatabaseType(DatabaseSetup databaseSetup) {
            this.database = databaseSetup;
        }
    }
}
