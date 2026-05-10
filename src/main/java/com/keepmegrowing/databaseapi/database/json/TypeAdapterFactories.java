
package com.keepmegrowing.databaseapi.database.json;

import com.google.gson.TypeAdapterFactory;
import com.keepmegrowing.databaseapi.database.json.DefaultTypeAdapterFactory;
import java.util.ArrayList;
import java.util.List;

public class TypeAdapterFactories {
    private static List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();

    public static List<TypeAdapterFactory> getTypeAdapterFactories() {
        return factories;
    }

    public static void addTypeAdapterFactory(TypeAdapterFactory typeAdapterFactory) {
        factories.add(typeAdapterFactory);
    }

    static {
        ArrayList<TypeAdapterFactory> arrayList = new ArrayList<TypeAdapterFactory>();
        arrayList.add(new DefaultTypeAdapterFactory());
        factories = arrayList;
    }
}
