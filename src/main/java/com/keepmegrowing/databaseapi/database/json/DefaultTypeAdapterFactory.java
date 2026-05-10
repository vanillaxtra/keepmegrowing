
package com.keepmegrowing.databaseapi.database.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.keepmegrowing.databaseapi.database.json.adapters.BukkitObjectTypeAdapter;
import com.keepmegrowing.databaseapi.database.json.adapters.ItemStackTypeAdapter;
import com.keepmegrowing.databaseapi.database.json.adapters.LocationTypeAdapter;
import com.keepmegrowing.databaseapi.database.json.adapters.PotionEffectTypeAdapter;
import com.keepmegrowing.databaseapi.database.json.adapters.VectorTypeAdapter;
import com.keepmegrowing.databaseapi.database.json.adapters.WorldTypeAdapter;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DefaultTypeAdapterFactory
implements TypeAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<?> raw = typeToken.getRawType();
        if (Location.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new LocationTypeAdapter();
        }
        if (ItemStack.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new ItemStackTypeAdapter();
        }
        if (PotionEffectType.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new PotionEffectTypeAdapter();
        }
        if (World.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new WorldTypeAdapter();
        }
        if (Vector.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new VectorTypeAdapter();
        }
        if (ConfigurationSerializable.class.isAssignableFrom(raw)) {
            return (TypeAdapter<T>) new BukkitObjectTypeAdapter((TypeAdapter<Map>) gson.getAdapter(Map.class));
        }
        return null;
    }
}
