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

    /*
    private static final String TAG = "EditActivity";
    private EditText mNameEditText;
    private EditText mDescriptionEditText;

    private Button editButton;
    private ImageButton removeButton;

    // Database objects
    private DatabaseReference mDatabase;

    private String name;
    private String description;
    private String device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        // Initialize all components
        editButton = (Button) findViewById(R.id.updateButton);
        removeButton = (ImageButton) findViewById(R.id.removeButton);
        mNameEditText = findViewById(R.id.itemName);
        mDescriptionEditText = findViewById(R.id.itemDescription);

        // Get extras from the previous intent,
        // which should be the MyItemActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            description = extras.getString("description");
            device = extras.getString("device");
        }

        mNameEditText.setText(name);
        mDescriptionEditText.setText(description);

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");

        HashMap<String, String> deviceIds = new HashMap<>();
        // Read from the database
        mUserItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap<String, Object>> items =
                        (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                Log.d(TAG, "Shit works somewhat");
                Log.d(TAG, "Items are: " + items);
                // Populate the arraylist with PENDING items
                for (String itemID: items.keySet()) {
                    deviceIds.put((String) items.get(itemID).get("device"), itemID);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Set listeners to open new intents in Android
        // Submit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit the items
                HashMap<String, Object> changes = new HashMap<>();
                name = mNameEditText.getText().toString();
                description = mDescriptionEditText.getText().toString();
                changes.put("/name", name);
                changes.put("/description", description);

                // Find the device ID with the given device

//                mUserItems.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        HashMap<String, HashMap<String, Object>> items =
//                                (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
//                        Log.d(TAG, "Shit works somewhat");
//                        Log.d(TAG, "Items are: " + items);
//                        // Populate the arraylist with PENDING items
//                        for (String itemID: items.keySet()) {
//                            if (! ((Boolean) items.get(itemID).get("pending")) ) {
//                                deviceIds.put((String) items.get(itemID).get("device"), itemID);
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w(TAG, "Failed to read value.", error.toException());
//                    }
//                });

                Log.d(TAG, "Devices are: " + deviceIds);

                for (String nextDevice: deviceIds.keySet()) {
                    if (nextDevice.equals(device)) {
                        Log.d(TAG, deviceIds.get(device));
                        mUserItems.child(deviceIds.get(device)).updateChildren(changes);
                    }
                }
                Toast.makeText(EditActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(EditActivity.this, ListItemsActivity.class);
                startActivity(findIntent);
            }
        });
    }

     */

}
