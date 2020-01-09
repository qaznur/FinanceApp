package ru.javabegin.tutorial.androidfinance.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class EditSourceActivity<T extends Source> extends AppCompatActivity {

    public static final String NODE_OBJECT = "ru.javabegin.tutorial.androidfinance.activities.EditSourceActivity.NodeObject";
    public static final int REQUEST_NODE_EDIT = 101;
    public static final int REQUEST_NODE_ADD = 103;
    public static final int REQUEST_CHILD_NODE_ADD = 104;

    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView imgSave;
    private ImageView closeIcon;
    private EditText etSprName;
    private Spinner spSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_source);

        setWindowAnimations();
        final T node = (T) getIntent().getSerializableExtra(NODE_OBJECT);
        initViews(node);
    }

    private void initViews(final T node) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSprName = findViewById(R.id.etSprName);
        tvTitle = findViewById(R.id.tv_title);

        if (node.getName() == null) {
            tvTitle.setText(R.string.adding);
            etSprName.setText("");
        } else {
            tvTitle.setText(R.string.editing);
            etSprName.setText(node.getName());
        }

        imgSave = findViewById(R.id.img_node_save);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etSprName.getText().toString();

                if (newName.trim().isEmpty()) {
                    Toast.makeText(EditSourceActivity.this, getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newName.equals(node.getName())) {
                    node.setName(newName);
                    node.setOperationType((OperationType) spSourceType.getSelectedItem());

                    Intent intent = new Intent();
                    intent.putExtra(NODE_OBJECT, node);
                    setResult(RESULT_OK, intent);
                }

                finishWithTransition();
            }
        });

        spSourceType = findViewById(R.id.sp_types);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, OperationType.getList().subList(0, 2));
        spSourceType.setAdapter(spinnerAdapter);
        if (node.getOperationType() != null) {
            spSourceType.setEnabled(false);
            spSourceType.setSelection(OperationType.getList().indexOf(node.getOperationType()));
        }

        closeIcon = findViewById(R.id.img_close);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithTransition();
            }
        });
    }

    private void setWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.setDuration(300);
            getWindow().setEnterTransition(slide);

            // при закрытии активити
            Slide slide2 = new Slide(Gravity.TOP);
            slide2.setDuration(500);
            getWindow().setExitTransition(slide2);
        }

    }

    private void finishWithTransition() {
        ActivityCompat.finishAfterTransition(EditSourceActivity.this);
    }
}
