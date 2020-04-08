package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seniorcare.R;
import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.User;

public class TokenActivity extends BaseActivity implements Button.OnClickListener{

    TextView tokenTextView;
    Button finishButton;
    SharedPreferences seniorCareSharedPref;
    SharedPreferences.Editor prefEditor;
    private static final String PREFNAME = "seniorCarePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        tokenTextView = findViewById(R.id.tokenTextView);
        String test = accountService.getCurrentUserPassCode();
        tokenTextView.setText(accountService.getCurrentUserPassCode());

        finishButton = findViewById(R.id.tokenFinishButton);
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        Intent seniorMenuIntent = new Intent(this, SeniorMenuActivity.class);
        startActivity(seniorMenuIntent);
    }
}