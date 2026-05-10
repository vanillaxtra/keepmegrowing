
package com.keepmegrowing.database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.keepmegrowing.objects.PlantBlock;
import java.io.IOException;

public class LocationCoordsTypeAdapter
extends TypeAdapter<PlantBlock> {
    @Override
    public void write(JsonWriter jsonWriter, PlantBlock block) throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value((long) block.blockX);
        jsonWriter.value((long) block.blockY);
        jsonWriter.value((long) block.blockZ);
        jsonWriter.endArray();
    }

    @Override
    public PlantBlock read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginArray();
        int x = jsonReader.nextInt();
        int y = jsonReader.nextInt();
        int z = jsonReader.nextInt();
        jsonReader.endArray();
        return new PlantBlock(x, y, z);
    }
}
