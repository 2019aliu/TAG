package com.example.tag;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
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

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// implements WifiP2pManager.ChannelListener, DeviceListFragment.DeviceActionListener
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private Switch mWifiSwitch;
    private Switch mBTSwitch;

    // Database objects
    private DatabaseReference mDatabase;

    // Bluetooth Connection
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

    // Wifi P2P Connection
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
//    private boolean retryChannel = false;
//    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            List<WifiP2pDevice> refreshedPeers = (ArrayList<WifiP2pDevice>) peerList.getDeviceList();
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);
            }

            if (peers.size() == 0) {
                Log.d(TAG, "No devices found");
                return;
            }
        }
    };

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

        // Initialize wifi P2P connection
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);


        // Submit button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btAddress = "00:99:C4:D1:CA:25";
                String wifiMAC = "00:b0:94:86:4e:6c";
                String deviceName = "HTC One X";

                // Make a new item
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                MyItem newItem = new MyItem(name, description, btAddress, wifiMAC, deviceName);

                // Register it to the database
                mUserItems.child(deviceName).updateChildren(newItem.toMap());

                // Bluetooth Enabling
                int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                // Currently paired devices
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device: pairedDevices) {
                        Log.d(TAG, "Name:" + device.getName() + ", address:" + device.getAddress());
                    }
                }

                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        // Code for when the discovery initiation is successful goes here.
                        // No services have actually been discovered yet, so this method
                        // can often be left blank. Code for peer discovery goes in the
                        // onReceive method, detailed below.
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        // Code for when the discovery initiation fails goes here.
                        // Alert the user that something went wrong.
                    }
                });


                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo;
                String ssid = "";
                wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo == null) {
                    Log.d(TAG, "No network connected!");
                } else {
                    Log.d(TAG, "SSID of the current WiFi network: " + wifiInfo.getSSID());
                }

                Log.d(TAG, "SSID of your wifi is: " + getMacAddr());
//                if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
//                    ssid = wifiInfo.getSSID();
//                }
//                Log.d(TAG, "SSID of the current WIFI network: " + ssid);

            }
        });
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }
//
//    /** register the BroadcastReceiver with the intent values to be matched */
//    @Override
//    public void onResume() {
//        super.onResume();
//        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
//        registerReceiver(receiver, intentFilter);
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        unregisterReceiver(receiver);
//    }
////
////    @Override
//    public void connect(WifiP2pConfig config) {
//        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
//            }
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(RegisterActivity.this, "Connect failed. Retry.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
