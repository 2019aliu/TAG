package com.example.tag;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class EditActivity  extends AppCompatActivity  {
    private static final String TAG = "RegisterActivity";
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private Switch mWifiSwitch;
    private Switch mBTSwitch;

    // Database objects
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.registerToolbar);
        toolbar.setTitle("Register an Item");
        setSupportActionBar(toolbar);

        // inflate all components, get the text
        mNameEditText = findViewById(R.id.itemName);
        mDescriptionEditText = findViewById(R.id.itemDescription);
        mWifiSwitch = (Switch) findViewById(R.id.wifi_switch);
        mBTSwitch = (Switch) findViewById(R.id.bt_switch);

        Bundle extras = getIntent().getExtras();
        String itemName = extras.getString("Item_Name");

        mNameEditText.setText(itemName);

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");


        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        // Submit button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                MyItem newItem = new MyItem(name, description, "00:99:C4:D1:CA:25", "00:b0:94:86:4e:6c");
                mUserItems.child(name).push().setValue(newItem);
                Log.d(TAG, "You registered an item with name "
                        + name + " and description " + description);

            }
        });
    }

}
