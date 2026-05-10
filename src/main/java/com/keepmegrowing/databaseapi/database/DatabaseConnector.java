
package com.keepmegrowing.databaseapi.database;

import org.eclipse.jdt.annotation.NonNull;

public interface DatabaseConnector {
    public Object createConnection(Class<?> var1);

    public void closeConnection(Class<?> var1);

    public String getConnectionUrl();

    public @NonNull String getUniqueId(String var1);

    public boolean uniqueIdExists(String var1, String var2);
}
