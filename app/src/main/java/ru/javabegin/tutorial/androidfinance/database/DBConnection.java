package ru.javabegin.tutorial.androidfinance.database;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.javabegin.tutorial.androidfinance.core.database.Initializer;
import ru.javabegin.tutorial.androidfinance.utils.MyApp;

public class DBConnection {

    private static final String TAG = MyApp.class.getName();

    private static final String DB_NAME = "money.db";
    private static final String DRIVER_CLASS = "org.sqldroid.SQLDroidDriver";

    private static String dbFolderPath;
    private static String dbPath;

    public static void initConnection(Context context) {
        checkDbExist(context);
        Initializer.load(DRIVER_CLASS, "jdbc:sqldroid:" + dbPath);
    }

    private static void checkDbExist(Context context)  {
        dbFolderPath = context.getApplicationInfo().dataDir + "/" + "database/";
        dbPath = dbFolderPath + DB_NAME;

        if(checkDatabaseExists()) {
            new File(dbPath).delete();
        }

        if (!checkDatabaseExists()) {
            File dbFolder = new File(dbFolderPath);
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
            }
            File dbFile = new File(dbPath);
            try {
                if (dbFile.createNewFile()) {
                    copyDatabase(context);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyDatabase(Context context) throws IOException {
        try (InputStream sourceFile = context.getAssets().open(DB_NAME);
             OutputStream destinationFolder = new FileOutputStream(dbPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceFile.read(buffer)) > 0) {
                destinationFolder.write(buffer, 0, length);
            }
        }
    }

    private static boolean checkDatabaseExists() {
        File dbFile = new File(dbPath);
        return dbFile.exists();
    }

}
