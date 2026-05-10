
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.io.IOException;

public class WorldTypeAdapter
extends TypeAdapter<World> {
    public void write(JsonWriter jsonWriter, World world) throws IOException {
        if (world == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(world.getName());
    }

    public World read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return Bukkit.getServer().getWorld(jsonReader.nextString());
    }
}
