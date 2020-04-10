package com.example.tag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tag.ui.login.LoginActivity;

public class ItemActivity  extends AppCompatActivity {

    private Button mapButton;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);

        Toolbar toolbar = findViewById(R.id.registerToolbar);
        Bundle extras = getIntent().getExtras();
        String itemName = extras.getString("Item_Name");
        toolbar.setTitle(itemName);
        setSupportActionBar(toolbar);

        editButton = (Button) findViewById(R.id.signup);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemActivity.this, "Sign in", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(ItemActivity.this, LoginActivity.class);
                startActivity(findIntent);
            }
        });
    }
}
