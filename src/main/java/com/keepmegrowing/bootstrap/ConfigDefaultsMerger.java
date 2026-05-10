package com.keepmegrowing.bootstrap;

import com.keepmegrowing.KeepMeGrowing;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import org.bukkit.configuration.file.YamlConfiguration;

public final class ConfigDefaultsMerger {

    private ConfigDefaultsMerger() {}

    public static void a(
            KeepMeGrowing plugin,
            String resourceName,
            File configFile,
            @SuppressWarnings("rawtypes") ArrayList ignorePrefixes) {
        YamlConfiguration def =
                YamlConfiguration.loadConfiguration(
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        plugin.getResource(resourceName),
                                        "Missing resource: " + resourceName),
                                StandardCharsets.UTF_8));
        YamlConfiguration user = YamlConfiguration.loadConfiguration(configFile);
        @SuppressWarnings("unchecked")
        ArrayList<String> prefixes = ignorePrefixes;
        boolean changed = false;
        keys:
        for (String key : def.getKeys(true)) {
            if (user.contains(key)) {
                continue;
            }
            for (String p : prefixes) {
                if (key.startsWith(p)) {
                    continue keys;
                }
            }
            user.set(key, def.get(key));
            changed = true;
        }
        if (changed) {
            try {
                user.save(configFile);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}
