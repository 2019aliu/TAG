package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.example.tag.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private String mTitle;
    private Button mFindButton;
    private Button mRegisterButton;
    private Button mEditButton;
    private Button mTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all buttons
        mFindButton = (Button) findViewById(R.id.button3);
        mRegisterButton = (Button) findViewById(R.id.button4);
//        mEditButton = (Button) findViewById(R.id.button5);

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

//        // Edit
//        mEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_SHORT).show();
//                Intent editIntent = new Intent(MainActivity.this, EditItemListActivity.class);
//                startActivity(editIntent);
//                // Do something in response to button click
//            }
//        });
    }
}
