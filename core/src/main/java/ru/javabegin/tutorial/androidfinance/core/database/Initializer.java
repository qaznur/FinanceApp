package ru.javabegin.tutorial.androidfinance.core.database;

import ru.javabegin.tutorial.androidfinance.core.dao.impls.OperationDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.dao.impls.SourceDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.dao.impls.StorageDAOImpl;
import ru.javabegin.tutorial.androidfinance.core.decorator.OperationSync;
import ru.javabegin.tutorial.androidfinance.core.decorator.SourceSync;
import ru.javabegin.tutorial.androidfinance.core.decorator.StorageSync;

public class Initializer {

    private static OperationSync operationSync;
    private static SourceSync sourceSync;
    private static StorageSync storageSync;

    public static void load(String driverName, String url) {
        SQLiteConnection.init(driverName, url);

        sourceSync = new SourceSync(new SourceDAOImpl());
        storageSync = new StorageSync(new StorageDAOImpl());
        operationSync = new OperationSync(new OperationDAOImpl(sourceSync.getIdentityMap(), storageSync.getIdendityMap()),
                sourceSync, storageSync);
    }


    public static OperationSync getOperationSync() {
        return operationSync;
    }

    public static SourceSync getSourceSync() {
        return sourceSync;
    }

    public static StorageSync getStorageSync() {
        return storageSync;
    }
}
