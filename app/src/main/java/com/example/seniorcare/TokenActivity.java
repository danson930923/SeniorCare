package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import seniorcare.db.sqlite.local.DbContext;
import seniorcare.models.User;

public class TokenActivity extends AppCompatActivity implements Button.OnClickListener{

    TextView tokenTextView;
    Button finishButton;
    SharedPreferences seniorCareSharedPref;
    SharedPreferences.Editor prefEditor;
    private static final String PREFNAME = "seniorCarePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        seniorCareSharedPref = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        prefEditor = seniorCareSharedPref.edit();

        tokenTextView = findViewById(R.id.tokenTextView);
        String username = seniorCareSharedPref.getString("USERNAME", "");
        User user = new User();
        user.setName(username);
        DbContext<User> userDbContext = new DbContext<>(this, user);
        User result = userDbContext.searchData(user).get(0);

        tokenTextView.setText(result.getPrimaryKeyValue().toString());

        finishButton = findViewById(R.id.tokenFinishButton);
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        Intent seniorMenuIntent = new Intent(this, SeniorMenuActivity.class);
        startActivity(seniorMenuIntent);
    }
}