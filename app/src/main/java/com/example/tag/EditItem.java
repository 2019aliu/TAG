package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditItem extends AppCompatActivity {

    private Button editButton;
    private ImageButton removeButton;

    // Database objects
    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        Bundle extras = getIntent().getExtras();
        String item = extras.getString("Item");

        // Initialize all buttons
        editButton = (Button) findViewById(R.id.updateButton);
        removeButton = (ImageButton) findViewById(R.id.removeButton);

        EditText itemName = (EditText) findViewById(R.id.itemName);
        EditText itemDescription = (EditText) findViewById(R.id.itemDescription);


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = mDatabase.getReference("test");


        // Set listeners to open new intents in Android
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.child("testUser/" + item + "/name").setValue(itemName.getText().toString());
                myref.child("testUser/" + item + "/description").setValue(itemDescription.getText().toString());
                Toast.makeText(EditItem.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(EditItem.this, ListItemsActivity.class);
                startActivity(findIntent);
            }
        });
    }
}
