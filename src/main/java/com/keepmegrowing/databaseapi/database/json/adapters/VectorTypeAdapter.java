
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.util.Vector;
import java.io.IOException;

public class VectorTypeAdapter
extends TypeAdapter<Vector> {
    public void write(JsonWriter jsonWriter, Vector vector) throws IOException {
        if (vector == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        jsonWriter.value(vector.getX());
        jsonWriter.value(vector.getY());
        jsonWriter.value(vector.getZ());
        jsonWriter.endArray();
    }

    public Vector read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginArray();
        double d = jsonReader.nextDouble();
        double d2 = jsonReader.nextDouble();
        double d3 = jsonReader.nextDouble();
        jsonReader.endArray();
        return new Vector(d, d2, d3);
    }
}
