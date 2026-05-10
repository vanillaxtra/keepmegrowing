
package com.keepmegrowing.database;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.keepmegrowing.database.ChunkCoordsTypeAdapter;
import com.keepmegrowing.database.LocationCoordsTypeAdapter;
import com.keepmegrowing.objects.ChunkKey;
import com.keepmegrowing.objects.PlantBlock;

public class KeepMeGrowingTypeAdapterFactory
implements TypeAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<?> raw = typeToken.getRawType();
        if (ChunkKey.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new ChunkCoordsTypeAdapter();
        }
        if (PlantBlock.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new LocationCoordsTypeAdapter();
        }
        return null;
    }
}
