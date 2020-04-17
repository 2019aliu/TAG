package com.example.tag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BTWifiActivity extends AppCompatActivity {

    private Button mSettingsButton;
    private Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btwifi);

        mSettingsButton = (Button) findViewById(R.id.button_BTWifiSettings);
        mContinueButton = (Button) findViewById(R.id.button_BTWifiContinue);

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                //pass any variables in here using .putExtra(), most likely user information
                startActivity(settingsIntent);
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager mWifiManager = (WifiManager)
                        getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mWifiManager.isWifiEnabled() && mBluetoothAdapter == null) {
                    Snackbar.make(v, "Device does not support bluetooth. Please turn on Wifi", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (mWifiManager.isWifiEnabled() || mBluetoothAdapter.isEnabled()) {
                    Snackbar.make(v, "Signal enabled. Searching...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent closeRangeIntent = new Intent(BTWifiActivity.this, CloseRangeActivity.class);
                    startActivity(closeRangeIntent);
                } else {
                    Snackbar.make(v, "Please turn on bluetooth or Wifi, or both", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(BTWifiActivity.this, ListItemsActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navigation_add:
                        Intent addIntent = new Intent(BTWifiActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_map:
                        break;
                }
                return false;
            }
        });
    }
}
