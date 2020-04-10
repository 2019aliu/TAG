package com.example.tag;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CloseRangeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private ImageButton taglightButton;
    private ImageButton tagvibrateButton;
    private Button mFoundButton;
    private CameraManager mCameraManager;
    private String mCameraId;

//    // Bluetooth Connection
//    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
//
//    // Wifi P2P Connection
//    private final IntentFilter intentFilter = new IntentFilter();
//    private WifiP2pManager.Channel channel;
//    private WifiP2pManager manager;
//    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
//    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private final int FLASH_REPS = 5;
    private final int FLASH_DURATION_MILLISECONDS = 3 * 1000;
    private final int VIBRATION_REPS = 5;
    private final int VIBRATION_DURATION_MILLISECONDS = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_range);
        builder = new AlertDialog.Builder(CloseRangeActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize all buttons
        taglightButton = (ImageButton) findViewById(R.id.taglightButton);
        tagvibrateButton = (ImageButton) findViewById(R.id.tagvibrateButton);
        mFoundButton = (Button) findViewById(R.id.founditem);


        // Set listeners to open new intents in Android
        // Find
        taglightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Camera2 package only available for Android API 23 and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Check flash availability
                    boolean isFlashAvailable = getApplicationContext().getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
                    if (!isFlashAvailable) {
                        showNoFlashError();
                    }

                    mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    try {
                        mCameraId = mCameraManager.getCameraIdList()[0];
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                    switchFlashLight(true);
                    new android.os.Handler().postDelayed(
                            () -> {
                                switchFlashLight(false);
                            },
                            FLASH_DURATION_MILLISECONDS);

                } else {
                    Camera cam = Camera.open();
                    Camera.Parameters p = cam.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    new android.os.Handler().postDelayed(
                            () -> {
                                cam.stopPreview();
                                cam.release();
                            },
                        FLASH_DURATION_MILLISECONDS);
                }
            }
        });

        tagvibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION_MILLISECONDS, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(VIBRATION_DURATION_MILLISECONDS);
                }
            }
        });

        mFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the buttons
                builder.setPositiveButton(R.string.confirmString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent finishIntent = new Intent(CloseRangeActivity.this, ListItemsActivity.class);
                        startActivity(finishIntent);
                    }
                });
                builder.setNegativeButton(R.string.cancelString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Snackbar.make(v, "Try walking around to help the close range detection algorithm", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                builder.setMessage(R.string.found_dialog_message)
                        .setTitle(R.string.found_dialog_title);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
}

//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo;
//                String ssid = "";
//                wifiInfo = wifiManager.getConnectionInfo();
//                if (wifiInfo == null) {
//                    Log.d(TAG, "No network connected!");
//                } else {
//                    Log.d(TAG, "SSID of the current WiFi network: " + wifiInfo.getSSID());
//                }
//
//                Log.d(TAG, "SSID of your wifi is: " + getMacAddr());
////                if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
////                    ssid = wifiInfo.getSSID();
////                }
////                Log.d(TAG, "SSID of the current WIFI network: " + ssid);

//                // Bluetooth Enabling
//                int REQUEST_ENABLE_BT = 1;
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//
//                // Currently paired devices
//                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//                if (pairedDevices.size() > 0) {
//                    for (BluetoothDevice device: pairedDevices) {
//                        Log.d(TAG, "Name:" + device.getName() + ", address:" + device.getAddress());
//                    }
//                }

//        // Initialize wifi P2P connection
//        // Indicates a change in the Wi-Fi P2P status.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//
//        // Indicates a change in the list of available peers.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//
//        // Indicates the state of Wi-Fi P2P connectivity has changed.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//
//        // Indicates this device's details have changed.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        channel = manager.initialize(this, getMainLooper(), null);

//    /**
//     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
//     */
//    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
//        this.isWifiP2pEnabled = isWifiP2pEnabled;
//    }
//
//    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
//        @Override
//        public void onPeersAvailable(WifiP2pDeviceList peerList) {
//
//            List<WifiP2pDevice> refreshedPeers = (ArrayList<WifiP2pDevice>) peerList.getDeviceList();
//            if (!refreshedPeers.equals(peers)) {
//                peers.clear();
//                peers.addAll(refreshedPeers);
//            }
//
//            if (peers.size() == 0) {
//                Log.d(TAG, "No devices found");
//                return;
//            }
//        }
//    };