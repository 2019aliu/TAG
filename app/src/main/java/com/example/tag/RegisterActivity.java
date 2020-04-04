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
                // Make a new item
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                MyItem newItem = new MyItem(name, description, "00:99:C4:D1:CA:25", "00:b0:94:86:4e:6c");

                // Register it to the database
                mDatabase.child(name).push();
                mUserItems.child(name).updateChildren(newItem.toMap());

                // Testing
                //    new MyItem("Phone", "Galaxy S9", "E8:99:C4:D1:CA:25", "1c:b0:94:86:4e:6c")
                //    new MyItem("Keys", "Dorm keys plus Explore lanyard", "00:00:00:00:00:00", "12:34:56:78:90:12")
                //    new MyItem("Wallet", "Buzzcard, debit, and cash", "11:22:33:44:55:66", "13:24:35:46:57:68")
                //    new MyItem("My sanity (rip)", "Help I've gone insane", "AA:BB:CC:DD:EE:FF", "10:29:38:47:56:65")

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

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
//
//    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }
            @Override
            public void onFailure(int reason) {
                Toast.makeText(RegisterActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private final BroadcastReceiver BTreceiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
//                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
//                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
//                Log.d(TAG, name + " => " + rssi + "dBm\n");
//            }
//        }
//    };

//
//    /**
//     * Remove all peers and clear all fields. This is called on
//     * BroadcastReceiver receiving a state change event.
//     */
//    public void resetData() {
//        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_list);
//        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_detail);
//        if (fragmentList != null) {
//            fragmentList.clearPeers();
//        }
//        if (fragmentDetails != null) {
//            fragmentDetails.resetViews();
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.action_items, menu);
//        return true;
//    }
//    /*
//     * (non-Javadoc)
//     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.atn_direct_enable:
//                if (manager != null && channel != null) {
//                    // Since this is the system wireless settings activity, it's
//                    // not going to send us a result. We will be notified by
//                    // WiFiDeviceBroadcastReceiver instead.
//                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                } else {
//                    Log.e(TAG, "channel or manager is null");
//                }
//                return true;
//            case R.id.atn_direct_discover:
//                if (!isWifiP2pEnabled) {
//                    Toast.makeText(RegisterActivity.this, R.string.p2p_off_warning,
//                            Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
//                        .findFragmentById(R.id.frag_list);
//                fragment.onInitiateDiscovery();
//                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(RegisterActivity.this, "Discovery Initiated",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onFailure(int reasonCode) {
//                        Toast.makeText(RegisterActivity.this, "Discovery Failed : " + reasonCode,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    @Override
//    public void showDetails(WifiP2pDevice device) {
//        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_detail);
//        fragment.showDetails(device);
//    }

//    @Override
//    public void disconnect() {
//        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_detail);
//        fragment.resetViews();
//        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onFailure(int reasonCode) {
//                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
//            }
//            @Override
//            public void onSuccess() {
//                fragment.getView().setVisibility(View.GONE);
//            }
//        });
//    }
//    @Override
//    public void onChannelDisconnected() {
//        // we will try once more
//        if (manager != null && !retryChannel) {
//            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
//            resetData();
//            retryChannel = true;
//            manager.initialize(this, getMainLooper(), this);
//        } else {
//            Toast.makeText(this,
//                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//    @Override
//    public void cancelDisconnect() {
//        /*
//         * A cancel abort request by user. Disconnect i.e. removeGroup if
//         * already connected. Else, request WifiP2pManager to abort the ongoing
//         * request
//         */
//        if (manager != null) {
//            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            if (fragment.getDevice() == null
//                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
//                disconnect();
//            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
//                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {
//                manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(RegisterActivity.this, "Aborting connection",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onFailure(int reasonCode) {
//                        Toast.makeText(RegisterActivity.this,
//                                "Connect abort request failed. Reason Code: " + reasonCode,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    }
//
//    public static String getTag() {
//        return TAG;
//    }



//    private class AcceptThread extends Thread {
//        private final BluetoothServerSocket mmServerSocket;
//
//        public AcceptThread() {
//            // Use a temporary object that is later assigned to mmServerSocket
//            // because mmServerSocket is final.
//            BluetoothServerSocket tmp = null;
//            try {
//                // MY_UUID is the app's UUID string, also used by the client code.
//                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's listen() method failed", e);
//            }
//            mmServerSocket = tmp;
//        }
//
//        public void run() {
//            BluetoothSocket socket = null;
//            // Keep listening until exception occurs or a socket is returned.
//            while (true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Log.e(TAG, "Socket's accept() method failed", e);
//                    break;
//                }
//
//                if (socket != null) {
//                    // A connection was accepted. Perform work associated with
//                    // the connection in a separate thread.
//                    manageMyConnectedSocket(socket);
//                    mmServerSocket.close();
//                    break;
//                }
//            }
//        }
//
//        // Closes the connect socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Could not close the connect socket", e);
//            }
//        }
//    }

    //        // Get the default bluetoothAdapter to store bonded devices into a Set of BluetoothDevice(s)
//        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        // It will work if your bluetooth device is already bounded to your phone
//        // If not, you can use the startDiscovery() method and connect to your device
//        Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
//
//        for (BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
//            bluetoothDevice.connectGatt(this, true, new BluetoothGattCallback() {
//                @Override
//                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//                    super.onConnectionStateChange(gatt, status, newState);
//                }
//                @Override
//                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//                    if(status == BluetoothGatt.GATT_SUCCESS)
//                        Log.d("BluetoothRssi", String.format("BluetoothGat ReadRssi[%d]", rssi));
//                }
//            });
//        }
}

// Unused code
// Discovering devices with bluetooth

//                // Bluetooth discoverer: Create a BroadcastReceiver for ACTION_FOUND.
//                final BroadcastReceiver receiver = new BroadcastReceiver() {
//                    public void onReceive(Context context, Intent intent) {
//                        String action = intent.getAction();
//                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                            // Discovery has found a device. Get the BluetoothDevice
//                            // object and its info from the Intent.
//                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
////                            String deviceName = device.getName();
////                            String deviceHardwareAddress = device.getAddress(); // MAC address
//                            Log.d(TAG, "Name:" + device.getName() + ", address:" + device.getAddress());
//                        }
//                    }
//                };
//
//
//                // Register for broadcasts when a device is discovered.
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                registerReceiver(receiver, filter);
//
//                // Don't forget to unregister the ACTION_FOUND receiver.
//                unregisterReceiver(receiver);





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

//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
//                assert wifiManager != null;
//                WifiInfo info = wifiManager.getConnectionInfo ();
//                String ssid  = info.getSSID();

//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION:
//                if  (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Log.e(TAG, "Fine location permission is not granted!");
//                    finish();
//                }
//                break;
//        }
//    }
//                // If an AdapterView is backed by this data, notify it
//                // of the change. For instance, if you have a ListView of
//                // available peers, trigger an update.
//                ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();

// Perform any other updates needed based on the new list of
// peers connected to the Wi-Fi P2P network.