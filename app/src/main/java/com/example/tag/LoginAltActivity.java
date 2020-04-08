package com.example.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class LoginAltActivity extends AppCompatActivity {

    private Button mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alt);

        // inflate the elements
        mSignin = (Button) findViewById(R.id.signin);

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginAltActivity.this, ListItemsActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
