
package com.keepmegrowing.databaseapi.database.transition;

import com.keepmegrowing.databaseapi.database.AbstractDatabaseHandler;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.jdt.annotation.Nullable;

public class TransitionDatabaseHandler<T>
extends AbstractDatabaseHandler<T> {
    private AbstractDatabaseHandler<T> fromHandler;
    private AbstractDatabaseHandler<T> toHandler;

    TransitionDatabaseHandler(Class<T> clazz, AbstractDatabaseHandler<T> abstractDatabaseHandler, AbstractDatabaseHandler<T> abstractDatabaseHandler2) {
        this.fromHandler = abstractDatabaseHandler;
        this.toHandler = abstractDatabaseHandler2;
    }

    @Override
    public List<T> loadObjects() {
        List<T> list = this.fromHandler.loadObjects();
        List<T> list2 = this.toHandler.loadObjects();
        for (T t2 : list) {
            this.toHandler.saveObject(t2);
            this.fromHandler.deleteObject(t2);
        }
        list2.addAll(list);
        return list2;
    }

    @Override
    public T loadObject(String string) {
        @Nullable T t2 = this.toHandler.loadObject(string);
        if (t2 == null && (t2 = this.fromHandler.loadObject(string)) != null) {
            this.toHandler.saveObject(t2);
            this.fromHandler.deleteObject(t2);
        }
        return t2;
    }

    @Override
    public boolean objectExists(String string) {
        return this.toHandler.objectExists(string) || this.fromHandler.objectExists(string);
    }

    @Override
    public CompletableFuture<Boolean> saveObject(T t2) {
        return this.toHandler.saveObject(t2);
    }

    @Override
    public void deleteID(String string) {
        this.toHandler.deleteID(string);
        this.fromHandler.deleteID(string);
    }

    @Override
    public void deleteObject(T t2) {
        this.toHandler.deleteObject(t2);
        this.fromHandler.deleteObject(t2);
    }

    @Override
    public void close() {
    }
}
