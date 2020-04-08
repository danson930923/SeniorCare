package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.seniorcare.R;

public class MainActivity extends BaseActivity implements Button.OnClickListener
{
    Button login, register;
    private static final String CLICKEDBTNTAG = "CLICKEDBTN";
    private static final String USERNAMETAG = "USERNAME";
    private static final String PREFNAME = "seniorCarePref";
    SharedPreferences seniorCareSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seniorCareSharedPref = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        if (!seniorCareSharedPref.getString(USERNAMETAG, "").equals("")) {
            if (seniorCareSharedPref.getString(USERNAMETAG, "").equals("General")) {
                Intent mainIntent = new Intent(this, GeneralMenuActivity.class);
                startActivity(mainIntent);
            } else if (seniorCareSharedPref.getString(USERNAMETAG, "").equals("Senior")) {
                Intent mainIntent = new Intent(this, SeniorMenuActivity.class);
                startActivity(mainIntent);
            }
        }

        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        try {
            Intent authIntent = new Intent(this, AuthActivity.class);
            if (v.getId() == R.id.loginButton) {
                authIntent.putExtra(CLICKEDBTNTAG, "Login");
            } else if (v.getId() == R.id.registerButton) {
                authIntent.putExtra(CLICKEDBTNTAG, "Register");
            } else {
                throw new Exception("Button not found");
            }
            startActivity(authIntent);
        } catch (Exception e) {
            Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
        }
    }

}