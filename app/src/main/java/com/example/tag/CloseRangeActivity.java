package com.example.tag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
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
import android.view.View;
import android.widget.ImageButton;

public class CloseRangeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private ImageButton mFlashButton;
    private ImageButton mVibrateButton;
    private CameraManager mCameraManager;
    private String mCameraId;

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

        mFlashButton = (ImageButton) findViewById(R.id.flashlight);
        mVibrateButton = (ImageButton) findViewById(R.id.vibration);

        mFlashButton.setOnClickListener(new View.OnClickListener() {
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

        mVibrateButton.setOnClickListener(new View.OnClickListener() {
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Add the buttons
                builder.setPositiveButton(R.string.confirmString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent finishIntent = new Intent(CloseRangeActivity.this, MainActivity.class);
                        startActivity(finishIntent);
                    }
                });
                builder.setNegativeButton(R.string.cancelString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Snackbar.make(view, "Try walking around to help the close range detection algorithm", Snackbar.LENGTH_LONG)
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
}
