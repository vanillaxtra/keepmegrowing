
package com.keepmegrowing.databaseapi.database.json;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import com.keepmegrowing.databaseapi.database.DatabaseConnector;
import com.keepmegrowing.databaseapi.database.DatabaseLogger;
import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.json.AbstractJSONDatabaseHandler;
import com.keepmegrowing.databaseapi.database.objects.DataObject;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.NonNull;

public class JSONDatabaseHandler<T>
extends AbstractJSONDatabaseHandler<T> {
    private static final String JSON = ".json";

    JSONDatabaseHandler(JavaPlugin javaPlugin, Class<T> clazz, DatabaseConnector databaseConnector, DatabaseSettings databaseSettings) {
        super(javaPlugin, clazz, databaseConnector, databaseSettings);
    }

    @Override
    public List<T> loadObjects() {
        ArrayList<T> arrayList = new ArrayList<T>();
        String object = this.dataObject.getSimpleName();
        File file2 = new File(this.plugin.getDataFolder(), "database");
        File file3 = new File(file2, object);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        for (File file4 : Objects.requireNonNull(file3.listFiles((file, string) -> string.toLowerCase(Locale.ENGLISH).endsWith(JSON)))) {
            try (FileReader exception = new FileReader(file4);){
                T object2 = this.getGson().fromJson((Reader)exception, this.dataObject);
                if (object2 != null) {
                    arrayList.add(object2);
                    continue;
                }
                DatabaseLogger.logError(this.plugin, "JSON file created a null object: " + file4.getPath());
                exception.close();
            }
            catch (FileNotFoundException fileNotFoundException) {
                DatabaseLogger.logError(this.plugin, "Could not load file '" + file4.getName() + "': File not found.");
            }
            catch (Exception exception2) {
                DatabaseLogger.logError(this.plugin, "Could not load objects " + file4.getName() + " " + exception2.getMessage());
            }
        }
        return arrayList;
    }

    @Override
    public T loadObject(@NonNull String string) {
        Object object = "database" + File.separator + this.dataObject.getSimpleName();
        string = (String)object + File.separator + string;
        if (!string.endsWith(JSON)) {
            string = string + JSON;
        }
        object = null;
        try (FileReader fileReader = new FileReader(new File(this.plugin.getDataFolder(), string));){
            object = this.getGson().fromJson((Reader)fileReader, this.dataObject);
        }
        catch (FileNotFoundException fileNotFoundException) {
            DatabaseLogger.logError(this.plugin, "Could not load file '" + string + "': File not found.");
        }
        catch (Exception exception) {
            DatabaseLogger.logError(this.plugin, "Could not load objects " + string + " " + exception.getMessage());
        }
        return (T)object;
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T object) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        if (object == null) {
            DatabaseLogger.logError(this.plugin, "JSON database request to store a null. ");
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        if (!(object instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + object.getClass().getName());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        final String subfolder = "database" + File.separator + this.dataObject.getSimpleName();
        final String fileName;
        try {
            PropertyDescriptor pd = new PropertyDescriptor("uniqueId", this.dataObject);
            Method readId = pd.getReadMethod();
            fileName = readId.invoke(object, new Object[0]) + JSON;
        } catch (Exception e) {
            DatabaseLogger.logError(this.plugin, "saveObject id: " + e.getMessage());
            completableFuture.complete(Boolean.FALSE);
            return completableFuture;
        }
        File dir = new File(this.plugin.getDataFolder(), subfolder);
        File file = new File(dir, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String json = this.getGson().toJson(object);
        if (this.plugin.isEnabled()) {
            this.processQueue.add(() -> this.store(completableFuture, json, file, dir, fileName));
        } else {
            this.store(completableFuture, json, file, dir, fileName);
        }
        return completableFuture;
    }

    private void store(CompletableFuture<Boolean> completableFuture, String string, File file, File file2, String string2) {
        try (FileWriter fileWriter = new FileWriter(file);){
            File file3 = new File(file2, string2 + ".bak");
            if (file.exists()) {
                Files.copy(file.toPath(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            fileWriter.write(string);
            Files.deleteIfExists(file3.toPath());
            completableFuture.complete(Boolean.TRUE);
        }
        catch (IOException iOException) {
            DatabaseLogger.logError(this.plugin, "Could not save JSON file: " + file2.getName() + " " + string2 + " " + iOException.getMessage());
            completableFuture.complete(Boolean.FALSE);
        }
    }

    @Override
    public void deleteID(String string) {
        if (this.plugin.isEnabled()) {
            this.processQueue.add(() -> this.delete(string));
            return;
        }
        this.delete(string);
    }

    private void delete(String id) {
        String fileName = id.endsWith(JSON) ? id : id + JSON;
        File base = new File(this.plugin.getDataFolder(), "database");
        File folder = new File(base, this.dataObject.getSimpleName());
        if (!folder.exists()) {
            return;
        }
        File target = new File(folder, fileName);
        try {
            Files.deleteIfExists(target.toPath());
        } catch (IOException e) {
            DatabaseLogger.logError(this.plugin, "Could not delete JSON database object! " + target.getName() + " - " + e.getMessage());
        }
    }

    @Override
    public void deleteObject(T t2) {
        if (t2 == null) {
            DatabaseLogger.logError(this.plugin, "JSON database request to delete a null.");
            return;
        }
        if (!(t2 instanceof DataObject)) {
            DatabaseLogger.logError(this.plugin, "This class is not a DataObject: " + t2.getClass().getName());
            return;
        }
        try {
            Method method = this.dataObject.getMethod("getUniqueId", new Class[0]);
            ((AbstractDatabaseHandler)this).deleteID((String)method.invoke(t2, new Object[0]));
            return;
        }
        catch (Exception exception) {
            DatabaseLogger.logError(this.plugin, "Could not delete object " + t2.getClass().getName() + " " + exception.getMessage());
            return;
        }
    }

    @Override
    public boolean objectExists(String string) {
        return this.databaseConnector.uniqueIdExists(this.dataObject.getSimpleName(), string);
    }

    @Override
    public void close() {
        this.shutdown = true;
    }
}
