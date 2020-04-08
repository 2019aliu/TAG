package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoggedOutActivity extends AppCompatActivity {

    private Button logOutButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_out);

        // Initialize all buttons
        logOutButton = (Button) findViewById(R.id.login);

        // Set listeners to open new intents in Android
        // Find
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoggedOutActivity.this, "Sign in", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(LoggedOutActivity.this, LoginAltActivity.class);
                startActivity(findIntent);
            }
        });

    }
}
