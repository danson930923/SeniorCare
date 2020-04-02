package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import seniorcare.db.sqlite.local.DbContext;
import seniorcare.models.User;

public class AuthActivity extends AppCompatActivity implements Button.OnClickListener{

    Button next;
    EditText username, password, phone;
    RadioButton generalUser, seniorUser;
    private static final String CLICKEDBTNTAG = "CLICKEDBTN";
    private static final String USERNAMETAG = "USERNAME";
    private static final String PASSWORDTAG = "PASSWORD";
    private static final String TYPETAG = "TYPE";
    private static final String PHONETAG = "PHONE";
    private static final String PREFNAME = "seniorCarePref";
    SharedPreferences seniorCareSharedPref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        phone = findViewById(R.id.phoneEditText);
        generalUser = findViewById(R.id.generalRadioButton);
        seniorUser = findViewById(R.id.seniorRadioButton);

        seniorCareSharedPref = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        prefEditor = seniorCareSharedPref.edit();

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        try {
            Intent parentIntent = getIntent();
            String parentAction = parentIntent.getStringExtra(CLICKEDBTNTAG);

            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();
            String phoneStr = phone.getText().toString();

            if (usernameStr.length() == 0 || passwordStr.length() == 0 || phoneStr.length() == 0 || (!generalUser.isChecked() && !seniorUser.isChecked())) {
                Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_SHORT).show();
                return;
            }

            // General User Registeration and Login
            if (generalUser.isChecked()) {
                boolean authrized = false;
                if (parentAction.equals("Register")) {
                    registerUser(usernameStr, passwordStr, phoneStr, "General");
                    authrized = true;
                } else if (loginUser()) { authrized = true; }
                if (authrized) {
                    Intent generalMenuIntent = new Intent(this, GeneralMenuActivity
                            .class);
                    startActivity(generalMenuIntent);
                }
            // Senior User Registeration and Login
            } else if (seniorUser.isChecked() && parentAction.equals("Register")) {
                registerUser(usernameStr, passwordStr, phoneStr, "Senior");
                Intent qrIntent = new Intent(this, TokenActivity.class);
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

    private void registerUser(String name, String pw, String phone, String type){
        prefEditor.putString(USERNAMETAG, name);
        prefEditor.putString(PASSWORDTAG, pw);
        prefEditor.putString(TYPETAG, type);

        //TO DO: DB
        DbContext<User> userDbContext = new DbContext<>(this, new User());
        User user = new User();
        user.setName(name);
        user.setPassword(pw);
        user.setType(type);
        prefEditor.putString(PHONETAG, phone);
        userDbContext.insertData(user);

        prefEditor.commit();

    }

    private boolean loginUser() {
         return true;
    }

    private void saveToSharedPreference(){

    }
}
