package com.example.tag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class EditActivity  extends AppCompatActivity  {
    final String TAG = "EditActivity";
    private Button editButton;
    private ImageButton removeButton;
    private AlertDialog.Builder builder;

    // Database objects
    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);
        builder = new AlertDialog.Builder(EditActivity.this);

        Bundle extras = getIntent().getExtras();
        String item_name = extras.getString("name");
        String item_description = extras.getString("description");
        String item_ID = extras.getString("id");

        // Initialize all buttons
        editButton = (Button) findViewById(R.id.updateButton);
        removeButton = (ImageButton) findViewById(R.id.removeButton);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName);
        EditText itemDescriptionEditText = (EditText) findViewById(R.id.itemDescription);
        itemNameEditText.setText(item_name);
        itemDescriptionEditText.setText(item_description);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(EditActivity.this, ListItemsActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navigation_add:
                        Intent addIntent = new Intent(EditActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_map:
                        Intent mapIntent = new Intent(EditActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return false;
            }
        });


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = mDatabase.getReference("test");


        // Set listeners to open new intents in Android
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, item_ID);
                myref.child("testUser/" + item_ID + "/name").setValue(itemNameEditText.getText().toString());
                myref.child("testUser/" + item_ID + "/description").setValue(itemDescriptionEditText.getText().toString());
                Toast.makeText(EditActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(EditActivity.this, ListItemsActivity.class);
                startActivity(findIntent);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the buttons
                builder.setPositiveButton(R.string.confirmString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myref.child("testUser").child(item_ID).removeValue();
                        Intent finishIntent = new Intent(EditActivity.this, ListItemsActivity.class);
                        startActivity(finishIntent);
                    }
                });
                builder.setNegativeButton(R.string.cancelString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.setMessage(R.string.delete_item_dialog_message)
                        .setTitle(R.string.delete_item_dialog_title);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


}
