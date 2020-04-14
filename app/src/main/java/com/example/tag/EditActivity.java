package com.example.tag;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class EditActivity  extends AppCompatActivity  {
    final String TAG = "EditActivity";
    private Button editButton;
    private ImageButton removeButton;

    // Database objects
    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        Bundle extras = getIntent().getExtras();
        String item = extras.getString("name");
        String id = extras.getString("id");

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
                Log.d(TAG, id);
                myref.child("testUser/" + id + "/name").setValue(itemName.getText().toString());
                myref.child("testUser/" + id + "/description").setValue(itemDescription.getText().toString());
                Toast.makeText(EditActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(EditActivity.this, ListItemsActivity.class);
                startActivity(findIntent);
            }
        });
    }


}
