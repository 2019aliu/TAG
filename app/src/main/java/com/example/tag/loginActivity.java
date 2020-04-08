package com.example.tag;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {

    private Button loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize all buttons
        loginButton = (Button) findViewById(R.id.signin);

        // Set listeners to open new intents in Android
        // Find
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(loginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(loginActivity.this, loginActivity.class);
                startActivity(findIntent);
            }
        });

    }
}
