package com.example.tag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private Button registerButton;

    // Database objects
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // inflate all components, get the text
        mNameEditText = findViewById(R.id.itemName);
        mDescriptionEditText = findViewById(R.id.itemDescription);
        registerButton = findViewById(R.id.registerButton);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(RegisterActivity.this, ListItemsActivity.class);
                        startActivity(homeIntent);
                    case R.id.navigation_add:
                        break;
                    case R.id.navigation_map:
                        Intent mapIntent = new Intent(RegisterActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return false;
            }
        });

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");

        // Set listeners to open new intents in Android
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Structures to hold all of the device names
                HashMap<String, String> deviceIds = new HashMap<>();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
//                builderSingle.setIcon(R.drawable.circle);
                builderSingle.setTitle("Select your device:");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.select_dialog_singlechoice);

                // Read from the database
                mUserItems.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        HashMap<String, HashMap<String, Object>> items =
                                (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                        // Populate the arraylist with PENDING items
                        for (String itemID : items.keySet()) {
                            if ((Boolean) items.get(itemID).get("pending")) {
                                arrayAdapter.add((String) items.get(itemID).get("device"));
                                deviceIds.put((String) items.get(itemID).get("device"), itemID);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(RegisterActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Make the map with name and description
                                HashMap<String, Object> changes = new HashMap<>();
                                String name = mNameEditText.getText().toString();
                                String description = mDescriptionEditText.getText().toString();
                                changes.put("/name", name);
                                changes.put("/description", description);
                                changes.put("/pending", false);

                                // "Register" the item to the database
                                // By updating the name and description fields
                                mUserItems.child(deviceIds.get(strName)).updateChildren(changes);
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                Intent findIntent = new Intent(RegisterActivity.this, ListItemsActivity.class);
                                startActivity(findIntent);
                            }
                        });
                        builderInner.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });
    }
}
