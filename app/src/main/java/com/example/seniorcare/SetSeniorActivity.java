package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetSeniorActivity extends AppCompatActivity implements Button.OnClickListener  {

    Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_senior);

    }

    @Override
    public void onClick (View v) {
        finish();
    }
}
