package ru.javabegin.tutorial.androidfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class EditSourceActivity<T extends Source> extends AppCompatActivity {

    public static final String NODE_OBJECT = "ru.javabegin.tutorial.androidfinance.activities.EditSourceActivity.NodeObject";
    public static int REQUEST_NODE_EDIT = 101;
    public static int REQUEST_NODE_ADD = 102;

    private Toolbar toolbar;
    private EditText etSprName;
    private ImageView imgSave;
    private Spinner spSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_source);

        final T node = (T) getIntent().getSerializableExtra(NODE_OBJECT);
        initViews(node);
    }

    private void initViews(final T node) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSprName  = findViewById(R.id.etSprName);
        if (node.getOperationType() != null) {
            etSprName.setText(node.getName());
        }

        imgSave = findViewById(R.id.img_node_save);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etSprName.getText().toString();

                if(newName.isEmpty()) {
                    Toast.makeText(EditSourceActivity.this, "Пустое название", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newName.equals(node.getName())) {
                    node.setName(newName);
                    Intent intent = new Intent();
                    intent.putExtra(NODE_OBJECT, node);
                    setResult(RESULT_OK, intent);
                }

                if(node.getOperationType() == null) {
                    node.setOperationType(OperationType.getList().get(spSourceType.getSelectedItemPosition()));
                }

                finish();
            }
        });

        spSourceType = findViewById(R.id.sp_types);
        if(node.getOperationType() != null) {
            spSourceType.setEnabled(false);
            spSourceType.setSelection(OperationType.getList().indexOf(node.getOperationType()));
        }
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, OperationType.getList().subList(0, 2));
        spSourceType.setAdapter(spinnerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
