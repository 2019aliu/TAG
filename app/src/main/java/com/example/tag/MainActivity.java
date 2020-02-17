package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String mTitle;
    private Button mFindButton;
    private Button mRegisterButton;
    private Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all buttons
        mFindButton = (Button) findViewById(R.id.button_find);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        mEditButton = (Button) findViewById(R.id.button_edit);

        // Set listeners to open new intents in Android
        // Find
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Find", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(MainActivity.this, ListItemsActivity.class);
                //pass any variables in here using .putExtra(), most likely user information
                startActivity(findIntent);
            }
        });

        // Register
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Register", Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                //pass any variables in here using .putExtra(), most likely user information
                startActivity(registerIntent);
            }
        });

        // Edit
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(editIntent);
                // Do something in response to button click
            }
        });
    }
}
