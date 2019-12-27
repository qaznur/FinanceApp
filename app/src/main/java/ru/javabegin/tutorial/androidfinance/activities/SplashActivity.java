package ru.javabegin.tutorial.androidfinance.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.database.DBConnection;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBConnection.initConnection(getApplicationContext());
                imitateLoading();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).start();
    }

    private void imitateLoading() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
