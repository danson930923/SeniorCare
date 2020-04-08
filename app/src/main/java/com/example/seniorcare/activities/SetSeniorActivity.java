package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seniorcare.R;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;
import com.example.seniorcare.services.ManageUserService;

public class SetSeniorActivity extends BaseActivity implements Button.OnClickListener  {

    Button finishButton;
    EditText tokenEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_senior);

        tokenEditText = findViewById(R.id.tokenEditText);
        finishButton = findViewById(R.id.tokenFinishButton);
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        if (tokenEditText.length() > 0){
            UserPassCode userPassCode = new UserPassCode();
            userPassCode.setPassCode( tokenEditText.getText().toString());
            managedUserService.addManagedUser(accountService.getCurrentUser(), userPassCode);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"Please fill in the field",Toast.LENGTH_SHORT).show();
        }
    }
}
