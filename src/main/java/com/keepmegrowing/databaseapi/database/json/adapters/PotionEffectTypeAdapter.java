
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.potion.PotionEffectType;
import java.io.IOException;

public class PotionEffectTypeAdapter
extends TypeAdapter<PotionEffectType> {
    public void write(JsonWriter jsonWriter, PotionEffectType potionEffectType) throws IOException {
        if (potionEffectType == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(potionEffectType.getName());
    }

    public PotionEffectType read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return PotionEffectType.getByName((String)jsonReader.nextString());
    }
}
