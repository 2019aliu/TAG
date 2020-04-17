package com.example.tag;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class CloseRangeActivity extends AppCompatActivity {

    private static final String TAG = "T∆G";

    private AlertDialog.Builder builder;
    private ImageButton taglightButton;
    private ImageButton tagvibrateButton;
    private Button mFoundButton;
    private CameraManager mCameraManager;
    private String mCameraId;

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    // Star means one to many
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;

    // Codename for now is just a constant
    // Can be randomly generated UUID or some other encrypted ID in the future
    private final String codeName = "TAGTracker";
//
//    private enum GameChoice {
//        ROCK,
//        PAPER,
//        SCISSORS;
//
//        boolean beats(GameChoice other) {
//            return (this == GameChoice.ROCK && other == GameChoice.SCISSORS)
//                    || (this == GameChoice.SCISSORS && other == GameChoice.PAPER)
//                    || (this == GameChoice.PAPER && other == GameChoice.ROCK);
//        }
//    }
    private String opponentEndpointId;
    //    private String otherEndpointID;
    private String opponentName;
    private int opponentScore;
//    private GameChoice opponentChoice;

    private int myScore;
//    private GameChoice myChoice;

    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
        new PayloadCallback() {
            @Override
            public void onPayloadReceived(String endpointId, Payload payload) {
//                opponentChoice = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
            }

            @Override
            public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
//                if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS && myChoice != null && opponentChoice != null) {
//                    finishRound();
//                }
            }
        };

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(codeName, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    // Callbacks for connections to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    opponentName = connectionInfo.getEndpointName();
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "onConnectionResult: connection successful");

                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();

                        opponentEndpointId = endpointId;
//                        setOpponentName(opponentName);
//                        setStatusText(getString(R.string.status_connected));
//                        setButtonState(true);
                    } else {
                        Log.i(TAG, "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i(TAG, "onDisconnected: disconnected from the opponent");
                    resetGame();
                }
            };

    private boolean isWifiP2pEnabled = false;

    private final int FLASH_REPS = 5;
    private final int FLASH_DURATION_MILLISECONDS = 3 * 1000;
    private final int VIBRATION_REPS = 5;
    private final int VIBRATION_DURATION_MILLISECONDS = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_range);
        builder = new AlertDialog.Builder(CloseRangeActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        // Initialize all buttons
        taglightButton = (ImageButton) findViewById(R.id.taglightButton);
        tagvibrateButton = (ImageButton) findViewById(R.id.tagvibrateButton);
        mFoundButton = (Button) findViewById(R.id.founditem);

        // Bottom Navigation Bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(CloseRangeActivity.this, ListItemsActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navigation_add:
                        Intent addIntent = new Intent(CloseRangeActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_map:
                        Intent mapIntent = new Intent(CloseRangeActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return false;
            }
        });

        // TODO: request user permissions for sending files and accessing fine location

        initializeUI();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanResults = wifiManager.getScanResults();
        for (ScanResult result: scanResults) {
            Log.d(TAG, String.format("SSID: %s, RSSI: %s", result.SSID, result.level));
            /*
            Deprecated in the most recent version of Android (as of April 13, 2020, so Android R)
            In the future, Android will no longer have relative levels of RSSI
            See the following link for more details:
            https://developer.android.com/reference/android/net/wifi/WifiManager#calculateSignalLevel(int)
             */
            int level = WifiManager.calculateSignalLevel(result.level, 10);
            Log.d(TAG, String.format("The WiFi strength for %s is: %s", result.SSID, level));
        }

        connectionsClient = Nearby.getConnectionsClient(this);

        resetGame();
        startAdvertising();
        startDiscovery();
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
//            }
//        }
//    }

    @Override
    protected void onStop() {
        connectionsClient.stopAllEndpoints();
//        resetGame();

        super.onStop();
    }

//    /** Returns true if the app was granted all the permissions. Otherwise, returns false. */
//    private static boolean hasPermissions(Context context, String... permissions) {
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(context, permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /** Handles user acceptance (or denial) of our permission request. */
//    @CallSuper
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
//            return;
//        }
//
//        for (int grantResult : grantResults) {
//            if (grantResult == PackageManager.PERMISSION_DENIED) {
//                Toast.makeText(this, R.string.error_missing_permissions, Toast.LENGTH_LONG).show();
//                finish();
//                return;
//            }
//        }
//        recreate();
//    }
//
//    /** Finds an opponent to play the game with using Nearby Connections. */
//    public void findOpponent(View view) {
//        startAdvertising();
//        startDiscovery();
////        setStatusText(getString(R.string.status_searching));
////        findOpponentButton.setEnabled(false);
//    }
//
    /** Disconnects from the opponent and reset the UI. */
    public void disconnect(View view) {
        connectionsClient.disconnectFromEndpoint(opponentEndpointId);
        resetGame();
    }

//    /** Sends a {@link GameChoice} to the other player. */
//    public void makeMove(View view) {
//        sendGameChoice(GameChoice.ROCK);
////        if (view.getId() == R.id.rock) {
////            sendGameChoice(GameChoice.ROCK);
////        } else if (view.getId() == R.id.paper) {
////            sendGameChoice(GameChoice.PAPER);
////        } else if (view.getId() == R.id.scissors) {
////            sendGameChoice(GameChoice.SCISSORS);
////        }
//    }

    /** Starts looking for other players using Nearby Connections. */
    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build());
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                codeName, getPackageName(), connectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build());
    }

    /** Wipes all game state and updates the UI accordingly. */
    private void resetGame() {
//        opponentEndpointId = null;
//        opponentName = null;
//        opponentChoice = null;
//        opponentScore = 0;
//        myChoice = null;
//        myScore = 0;

//        setOpponentName(getString(R.string.no_opponent));
//        setStatusText(getString(R.string.status_disconnected));
//        updateScore(myScore, opponentScore);
//        setButtonState(false);
    }
//
//    /** Sends the user's selection of rock, paper, or scissors to the opponent. */
//    private void sendGameChoice(GameChoice choice) {
//        myChoice = choice;
//        connectionsClient.sendPayload(
//                opponentEndpointId, Payload.fromBytes(choice.name().getBytes(UTF_8)));
//
////        setStatusText(getString(R.string.game_choice, choice.name()));
//        // No changing your mind!
////        setGameChoicesEnabled(false);
//    }
//
//    /** Determines the winner and update game state/UI after both players have chosen. */
//    private void finishRound() {
//        if (myChoice.beats(opponentChoice)) {
//            // Win!
////            setStatusText(getString(R.string.win_message, myChoice.name(), opponentChoice.name()));
//            myScore++;
//        } else if (myChoice == opponentChoice) {
//            // Tie, same choice by both players
////            setStatusText(getString(R.string.tie_message, myChoice.name()));
//        } else {
//            // Loss
////            setStatusText(getString(R.string.loss_message, myChoice.name(), opponentChoice.name()));
//            opponentScore++;
//        }
//
//        myChoice = null;
//        opponentChoice = null;
//
////        updateScore(myScore, opponentScore);
////
////        // Ready for another round
////        setGameChoicesEnabled(true);
//    }
//
////    /** Enables/disables buttons depending on the connection status. */
////    private void setButtonState(boolean connected) {
////        findOpponentButton.setEnabled(true);
////        findOpponentButton.setVisibility(connected ? View.GONE : View.VISIBLE);
////        disconnectButton.setVisibility(connected ? View.VISIBLE : View.GONE);
////
////        setGameChoicesEnabled(connected);
////    }
////
////    /** Enables/disables the rock, paper, and scissors buttons. */
////    private void setGameChoicesEnabled(boolean enabled) {
////        rockButton.setEnabled(enabled);
////        paperButton.setEnabled(enabled);
////        scissorsButton.setEnabled(enabled);
////    }
////
////    /** Shows a status message to the user. */
////    private void setStatusText(String text) {
////        statusText.setText(text);
////    }
////
////    /** Updates the opponent name on the UI. */
////    private void setOpponentName(String opponentName) {
////        opponentText.setText(getString(R.string.opponent_name, opponentName));
////    }
////
////    /** Updates the running score ticker. */
////    private void updateScore(int myScore, int opponentScore) {
////        scoreText.setText(getString(R.string.game_score, myScore, opponentScore));
////    }

    /*
    Helper methods to activate flash and vibrate
     */

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

    public void initializeUI() {
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


    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
}

//        ScanResult result0 = scanResults.get(0);
//        String ssid0 = result0.SSID;
//        int rssi0 = result0.level;
//        String rssiString0 = String.valueOf(rssi0);
//
//        Log.d(TAG, String.format("SSID: %s, RSSI: %s", ssid0, rssiString0));

//        int numberOfLevels = 5;
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//        Log.d(TAG, "The WiFi Strength level is: " + level);

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

//    // Bluetooth Connection
//    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
//
//    // Wifi P2P Connection
//    private final IntentFilter intentFilter = new IntentFilter();
//    private WifiP2pManager.Channel channel;
//    private WifiP2pManager manager;
//    private BroadcastReceiver receiver = null;
//    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();