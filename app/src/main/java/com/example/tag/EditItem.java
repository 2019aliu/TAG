package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditItem extends AppCompatActivity {

    private Button editButton;
    private ImageButton removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        // Initialize all buttons
        editButton = (Button) findViewById(R.id.updateButton);
        removeButton = (ImageButton) findViewById(R.id.removeButton);

        // Set listeners to open new intents in Android
        // Find
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditItem.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(EditItem.this, ListItemsActivity.class);
                startActivity(findIntent);
            }
        });
    }
}
