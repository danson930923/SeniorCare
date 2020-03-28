package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity implements Button.OnClickListener{

    Button next;
    EditText username, password;
    RadioButton generalUser, seniorUser;
    static final String CLICKEDBTNTAG = "CLICKEDBTN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
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

            // General User Registeration and Login
            if (generalUser.isChecked()) {
                boolean authrized = false;
                if (parentAction.equals("Register")) {
                    registerUser();
                    authrized = true;
                } else if (loginUser()) {
                    authrized = true;
                }
                if (authrized) {
                    Intent generalMenuIntent = new Intent(this, GeneralMenuActivity
                            .class);
                    startActivity(generalMenuIntent);
                }
                // Senior User Registeration and Login
            } else if (seniorUser.isChecked() && parentAction.equals("Register")) {
                Intent qrIntent = new Intent(this, QRActivity.class);
                startActivity(qrIntent);
            } else if (seniorUser.isChecked() && parentAction.equals("Login")) {
                Intent seniorMenuIntent = new Intent(this, SeniorMenuActivity.class);
                startActivity(seniorMenuIntent);
            } else {
                Toast.makeText(getApplicationContext(),"Please select your user type.",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
        }
    }

    private void registerUser(){}

    private boolean loginUser() {
        return true;
    }
}