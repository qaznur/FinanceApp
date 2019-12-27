package ru.javabegin.tutorial.androidfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.fragments.TreeNodeAdapter;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sprName = getIntent().getStringExtra(TreeNodeAdapter.SPR_NAME);
        EditText etSprName = findViewById(R.id.etSprName);
        etSprName.setText(sprName);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
