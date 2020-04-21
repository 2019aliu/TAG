package com.example.tag;

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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CloseRangeActivity extends AppCompatActivity {

    private static final String TAG = "CloseRangeActivity";
    private final int FLASH_REPS = 5;
    private final int FLASH_DURATION_MILLISECONDS = 3 * 1000;
    private final int VIBRATION_REPS = 5;
    private final int VIBRATION_DURATION_MILLISECONDS = 3 * 1000;
    private AlertDialog.Builder builder;
    private ImageButton taglightButton;
    private ImageButton tagvibrateButton;
    private Button mFoundButton;
    private CameraManager mCameraManager;
    private String mCameraId;
    private boolean isWifiP2pEnabled = false;

    private MessageListener mMessageListener;
    private Message mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_range);
        builder = new AlertDialog.Builder(CloseRangeActivity.this);

        // Initialize all buttons
        taglightButton = findViewById(R.id.taglightButton);
        tagvibrateButton = findViewById(R.id.tagvibrateButton);
        mFoundButton = findViewById(R.id.founditem);

        // Bottom Navigation Bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
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
        for (ScanResult result : scanResults) {
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

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };

        mMessage = new Message("Hello World from TAG".getBytes());
    }

    @Override
    public void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).publish(mMessage);
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    public void onStop() {
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }

    private void publish(String message) {
        Log.i(TAG, "Publishing message: " + message);
        mMessage = new Message(message.getBytes());
        Nearby.getMessagesClient(this).publish(mMessage);
    }

    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        if (mMessage != null) {
            Nearby.getMessagesClient(this).unpublish(mMessage);
            mMessage = null;
        }
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
    }

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
