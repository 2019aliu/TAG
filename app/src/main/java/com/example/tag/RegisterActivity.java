package com.example.tag;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
//    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.registerToolbar);
        toolbar.setTitle("Register an Item");
        setSupportActionBar(toolbar);

        // inflate all components, get the text
        mNameEditText = findViewById(R.id.itemName);
        String name = mNameEditText.getText().toString();
        mDescriptionEditText = findViewById(R.id.itemDescription);
        String description = mDescriptionEditText.getText().toString();
//        mRegisterButton = findViewById(R.id.registerNewItemButton);

//        mRegisterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                Log.d(TAG, "You registered an item with name "
                        + name + " and description " + description);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }
}
