
package com.keepmegrowing.database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.keepmegrowing.objects.ChunkKey;
import java.io.IOException;

public class ChunkCoordsTypeAdapter
extends TypeAdapter<ChunkKey> {
    @Override
    public void write(JsonWriter jsonWriter, ChunkKey key) throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value((long) key.chunkX);
        jsonWriter.value((long) key.chunkZ);
        jsonWriter.value(key.worldName);
        jsonWriter.endArray();
    }

    @Override
    public ChunkKey read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginArray();
        int chunkX = jsonReader.nextInt();
        int chunkZ = jsonReader.nextInt();
        String worldName = jsonReader.nextString();
        jsonReader.endArray();
        return new ChunkKey(chunkX, chunkZ, worldName);
    }
}
