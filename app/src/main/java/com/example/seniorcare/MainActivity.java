package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener
{
    Button login, register;
    final String CLICKEDBTNTAG = "CLICKEDBTN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
