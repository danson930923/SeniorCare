package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class AuthActivity extends AppCompatActivity implements Button.OnClickListener{

    Button next;
    RadioButton generalUser, seniorUser;
    static final String CLICKEDBTNTAG = "CLICKEDBTN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        generalUser = findViewById(R.id.generalRadioButton);
        seniorUser = findViewById(R.id.seniorRadioButton);

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        try {
            Intent parentIntent = getIntent();
            String parentAction = parentIntent.getStringExtra(CLICKEDBTNTAG);

            if (generalUser.isChecked()) {
                Intent mainIntent = new Intent(this, AuthActivity.class);
                startActivity(mainIntent);
            } else if (seniorUser.isChecked() && parentAction == "Register") {
                Intent qrIntent = new Intent(this, QRActivity.class);
                startActivity(qrIntent);
            }
        } catch (Exception e) {
            Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
        }
    }
}
