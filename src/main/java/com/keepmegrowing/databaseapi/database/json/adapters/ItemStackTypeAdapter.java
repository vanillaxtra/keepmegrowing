
package com.keepmegrowing.databaseapi.database.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.IOException;

public class ItemStackTypeAdapter
extends TypeAdapter<ItemStack> {
    public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
        if (itemStack == null) {
            jsonWriter.nullValue();
            return;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("is", (Object)itemStack);
        jsonWriter.value(yamlConfiguration.saveToString());
    }

    public ItemStack read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        String raw = in.nextString();
        if (raw.contains("type:")) {
            String rest = raw.substring(raw.indexOf("type:") + 6);
            rest = rest.substring(0, rest.indexOf('\n'));
            Material material = Material.matchMaterial(rest);
            if (material == null) {
                DatabaseLogger.logWarning("Unknown material: " + rest);
                return new ItemStack(Material.AIR);
            }
        }
        try {
            yamlConfiguration.loadFromString(raw);
            return yamlConfiguration.getItemStack("is");
        } catch (InvalidConfigurationException invalidConfigurationException) {
            DatabaseLogger.logError("Cannot load ItemStack serialized as " + raw);
            return new ItemStack(Material.AIR);
        }
    }
}
