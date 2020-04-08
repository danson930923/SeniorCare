package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.seniorcare.R;
import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.ContactInfo;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;
import com.example.seniorcare.services.AccountService;

public class AuthActivity extends BaseActivity implements Button.OnClickListener{

    Button next;
    EditText username, password, phone;
    RadioButton generalUser, seniorUser;
    private static final String CLICKEDBTNTAG = "CLICKEDBTN";
    private static final String PHONETAG = "PHONE";
    private static final String SENIOR_CARE_PREF_NAME = "seniorCarePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        phone = findViewById(R.id.phoneEditText);
        generalUser = findViewById(R.id.youngRadioButton);
        seniorUser = findViewById(R.id.seniorRadioButton);

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
                } else if (parentAction.equals("Login")) {
                    authrized = loginUser(usernameStr, passwordStr);
                }
                if (authrized) {
                    Intent generalMenuIntent = new Intent(this, GeneralMenuActivity
                            .class);
                    startActivity(generalMenuIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect username or password.",Toast.LENGTH_SHORT).show();
                }
            // Senior User Registeration and Login
            } else if (seniorUser.isChecked() && parentAction.equals("Register")) {
                registerUser(usernameStr, passwordStr, phoneStr, "Senior");
                Intent qrIntent = new Intent(this, TokenActivity.class);
                startActivity(qrIntent);
            } else if (seniorUser.isChecked() && parentAction.equals("Login")) {
                boolean authrized = loginUser(usernameStr, passwordStr);
                if (authrized) {
                    Intent seniorMenuIntent = new Intent(this, SeniorMenuActivity.class);
                    startActivity(seniorMenuIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect username or password.",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Please select your user type.",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MAIN_BTN_CLICK_ERR ", e.getMessage());
        }
    }

    private void registerUser(String name, String pw, String phone, String type){

        User user = new User();
        user.setName(name);
        user.setPassword(pw);
        user.setType(type);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUserId(user.getUserId());
        contactInfo.setPhoneNumber(phone);

        //TO DO: DB
        accountService.register(user, contactInfo);
    }

    private boolean loginUser(String username, String password) {
        User loginUser = new User();
        loginUser.setName(username);
        loginUser.setPassword(password);
        boolean test = accountService.login(loginUser);
        return test;
    }
}
