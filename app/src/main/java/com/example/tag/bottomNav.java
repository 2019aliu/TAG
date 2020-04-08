package com.example.tag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tag.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class bottomNav extends AppCompatActivity {

    private Button homeButton;
    private Button mapButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Initialize all buttons
        homeButton = (Button) findViewById(R.id.navigation_home);
        mapButton = (Button) findViewById(R.id.navigation_dashboard);
        settingsButton = (Button) findViewById(R.id.navigation_notifications);

        // Set listeners to open new intents in Android
        // Find
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(bottomNav.this, "TAG", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(bottomNav.this, ListItemsActivity.class);
                startActivity(findIntent);

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(bottomNav.this, "Map", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(bottomNav.this, GPSFindActivity.class);
                startActivity(findIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(bottomNav.this, "Settings", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(bottomNav.this, SettingsActivity.class);
                startActivity(findIntent);
            };

        });
    }
}