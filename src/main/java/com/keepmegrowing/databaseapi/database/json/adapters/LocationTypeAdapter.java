
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import java.io.IOException;

public class LocationTypeAdapter
extends TypeAdapter<Location> {
    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        if (location == null || location.getWorld() == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        jsonWriter.value(location.getWorld().getName());
        jsonWriter.value(location.getX());
        jsonWriter.value(location.getY());
        jsonWriter.value(location.getZ());
        jsonWriter.value((double)location.getYaw());
        jsonWriter.value((double)location.getPitch());
        jsonWriter.endArray();
    }

    public Location read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginArray();
        World world = Bukkit.getServer().getWorld(jsonReader.nextString());
        double d = jsonReader.nextDouble();
        double d2 = jsonReader.nextDouble();
        double d3 = jsonReader.nextDouble();
        float f = (float)jsonReader.nextDouble();
        float f2 = (float)jsonReader.nextDouble();
        jsonReader.endArray();
        return new Location(world, d, d2, d3, f, f2);
    }
}
