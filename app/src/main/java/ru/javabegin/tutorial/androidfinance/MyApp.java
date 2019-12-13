package ru.javabegin.tutorial.androidfinance;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyApp extends Application {

    private static final String TAG = MyApp.class.getName();

    private static final String DB_NAME = "money.db";
    private static String dbFolderPath;
    private static String dbPath;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            checkDbExist(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkDbExist(Context context) throws IOException {
        dbFolderPath = context.getApplicationInfo().dataDir + "/" + "database/";
        dbPath = dbFolderPath + DB_NAME;
        if (!checkDatabaseExists()) {
            File dbFolder = new File(dbFolderPath);
            if (!dbFolder.exists()) {
                if (dbFolder.mkdirs()) {
                    File dbFile = new File(dbPath);
                    if (dbFile.createNewFile()) {
                        copyDatabase(context);
                    }
                }
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
