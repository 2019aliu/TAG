package com.example.tag;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
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

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");
        // Test connection

//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    Log.d(TAG, "connected");
//                } else {
//                    Log.d(TAG, "not connected");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "Listener was cancelled");
//            }
//        });

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
//                mDatabase.setValue(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                MyItem newItem = new MyItem(name, description, "E8:99:C4:D1:CA:25", "1c:b0:94:86:4e:6c");
                mUserItems.child(name).setValue(newItem);
                Log.d(TAG, "You registered an item with name "
                        + name + " and description " + description);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


//                /*
//                My attempt at bluetooth scanning/
//                References:
//                - https://stuff.mit.edu/afs/sipb/project/android/docs/guide/topics/connectivity/bluetooth.html#FindingDevices
//                - (Android official docs) https://developer.android.com/reference/android/bluetooth/BluetoothDevice
//                - (Also official docs) https://developer.android.com/reference/android/bluetooth/BluetoothAdapter
//                 */
//
//                final ArrayList<String> mArrayAdapter = new ArrayList<>();
//
//                // Bluetooth/Wifi Scanning
//                if (mBTSwitch.isChecked()) {
//                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    // Create a BroadcastReceiver for ACTION_FOUND
//                    // can't make it private in here for some reason oh well
//                    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//                        public void onReceive(Context context, Intent intent) {
//                            String action = intent.getAction();
//                            // When discovery finds a device
//                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                                // Get the BluetoothDevice object from the Intent
//                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                                // Add the name and address to an array adapter to show in a ListView
//                                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                            }
//                        }
//                    };
//                    // Register the BroadcastReceiver
//                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                    registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//                    for (String item: mArrayAdapter) {
//                        Log.d(TAG, item);
//                    }
//
//
////                    mBluetoothAdapter.getRemoteDevice("E8:99:C4:D1:CA:25");
//
////                    Log.d(TAG, "You want Bluetooth!");
//                }
            }
        });
    }
}
