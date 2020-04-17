package com.example.tag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyItemActivity extends AppCompatActivity {

    private ImageButton editButton;
    private Button findButton;

    private String id;
    private String name;
    private String description;
    private String btAddress;
    private String wifiMAC;
    private String device;
    private double destLatitude;
    private double destLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);
        Toolbar toolbar = findViewById(R.id.myitem_toolbar);
        toolbar.setTitle("T∆G");
        System.out.println(toolbar.getTitle());
        setSupportActionBar(toolbar);

        // Get extras from the previous intent,
        // which should be the listitemsActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            name = extras.getString("name");
            description = extras.getString("description");
            btAddress = extras.getString("btAddress");
            wifiMAC = extras.getString("wifiMAC");
            device = extras.getString("device");
            destLatitude = extras.getDouble("latitude");
            destLongitude = extras.getDouble("longitude");
        }

        // Initialize all buttons
        editButton = (ImageButton) findViewById(R.id.editButton);
        findButton = (Button) findViewById(R.id.locate_button);

        // Set listeners to open new intents in Android
        // Find
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyItemActivity.this, "Edit Item", Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(MyItemActivity.this,EditActivity.class);
                editIntent.putExtra("name", name);
                editIntent.putExtra("description", description);
                editIntent.putExtra("id", id);
                startActivity(editIntent);
            }
        });


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                System.out.println(String.format("Latitude: %s, Longitude: %s", destLatitude, destLongitude));
                String queryDestination = String.format("%s, %s", destLatitude, destLongitude);
                Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s", queryDestination));

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    // Open up the next activity first
                    Intent BTWifiIntent = new Intent(MyItemActivity.this, BTWifiActivity.class);
                    startActivity(BTWifiIntent);
                    // And then open up Google Maps
                    startActivity(mapIntent);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.myitem_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_add:
                        Intent addIntent = new Intent(MyItemActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_map:
                        Intent mapIntent = new Intent(MyItemActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return false;
            }
        });
    }
}
