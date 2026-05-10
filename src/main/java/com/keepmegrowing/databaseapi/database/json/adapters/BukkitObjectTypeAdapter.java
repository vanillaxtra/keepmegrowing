
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class BukkitObjectTypeAdapter
extends TypeAdapter<ConfigurationSerializable> {
    private final TypeAdapter<Map> map;

    public BukkitObjectTypeAdapter(TypeAdapter<Map> typeAdapter) {
        this.map = typeAdapter;
    }

    public static Map<String, Object> serializeObject(@NonNull ConfigurationSerializable configurationSerializable) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(configurationSerializable.serialize());
        hashMap.entrySet().stream().filter(entry -> entry.getValue() instanceof ConfigurationSerializable).forEach(entry -> hashMap.put((String)entry.getKey(), BukkitObjectTypeAdapter.serializeObject((ConfigurationSerializable)entry.getValue())));
        hashMap.put("==", ConfigurationSerialization.getAlias((Class)configurationSerializable.getClass()));
        return hashMap;
    }

    public @Nullable ConfigurationSerializable deserializeObject(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        for (Map.Entry<String, Object> entry : new HashMap<>(map).entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nested = (Map<String, Object>)val;
                if (nested.containsKey("==")) {
                    map.put(entry.getKey(), deserializeObject(nested));
                }
            }
        }
        return ConfigurationSerialization.deserializeObject(map);
    }

    public @Nullable ConfigurationSerializable read(JsonReader jsonReader) throws IOException {
        BukkitObjectTypeAdapter bukkitObjectTypeAdapter = this;
        return bukkitObjectTypeAdapter.deserializeObject((Map)bukkitObjectTypeAdapter.map.read(jsonReader));
    }

    public void write(JsonWriter jsonWriter, ConfigurationSerializable configurationSerializable) throws IOException {
        if (configurationSerializable == null) {
            jsonWriter.nullValue();
            return;
        }
        this.map.write(jsonWriter, BukkitObjectTypeAdapter.serializeObject(configurationSerializable));
    }
}
