package com.example.tag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CloseRangeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private ImageButton taglightButton;
    private ImageButton tagvibrateButton;
//    private ImageButton mFlashButton;
//    private ImageButton mVibrateButton;

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


        // Set listeners to open new intents in Android
        // Find
        taglightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloseRangeActivity.this, "Light On", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(CloseRangeActivity.this,CloseRangeActivity.class);
                startActivity(findIntent);
            }
        });

        tagvibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloseRangeActivity.this, "Vibrating", Toast.LENGTH_SHORT).show();
                Intent findIntent = new Intent(CloseRangeActivity.this,CloseRangeActivity.class);
                startActivity(findIntent);
            }
        });


//        mFlashButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        mVibrateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                // Vibrate for 500 milliseconds
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    //deprecated in API 26
//                    vibrator.vibrate(500);
//                }
//            }
//        });
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                // Add the buttons
//                builder.setPositiveButton(R.string.found_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent finishIntent = new Intent(CloseRangeActivity.this, MainActivity.class);
//                        startActivity(finishIntent);
//                    }
//                });
//                builder.setNegativeButton(R.string.found_cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        Snackbar.make(view, "Try walking around to help the close range detection algorithm", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
//                });
//                builder.setMessage(R.string.found_dialog_message)
//                        .setTitle(R.string.found_dialog_title);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
    }

}
