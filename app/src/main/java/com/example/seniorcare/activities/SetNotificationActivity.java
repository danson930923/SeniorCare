package com.example.seniorcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.seniorcare.R;
import com.example.seniorcare.models.Reminder;

import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;

import static com.example.seniorcare.activities.Constant.REMINDER_ID;
import static com.example.seniorcare.activities.Constant.SENIOR_NAME;
import static com.example.seniorcare.activities.Constant.SENIOR_USER_ID;

public class SetNotificationActivity extends BaseActivity implements View.OnClickListener {

    Button notificationFinishButton;
    EditText titleEditText;
    EditText timeEditText;
//    private String seniorName;
    private String seniorUserId;

    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        init();
    }

    private void init() {

        setContentView(R.layout.activity_set_notification);
        notificationFinishButton = findViewById(R.id.notificationFinishButton);
        notificationFinishButton.setOnClickListener(this);
        titleEditText = findViewById(R.id.titleEditText);
        timeEditText = findViewById(R.id.timeEditText);


        Intent intent = getIntent();
        seniorUserId = intent.getStringExtra(SENIOR_USER_ID);
        String reminderId = intent.getStringExtra(REMINDER_ID);
        reminder = reminderService.getReminderById(reminderId);
        if (reminder == null) {
            return;
        }

        titleEditText.setText(reminder.getTitle());
        timeEditText.setText(reminderService.getReminderDisplayTime(reminder));
    }

    @Override
    public void onClick(View v) {

        String title = titleEditText.getText().toString();
        String time = timeEditText.getText().toString();

        Reminder newReminder = new Reminder();
        newReminder.setTime(time);
        newReminder.setUserId(seniorUserId);
        newReminder.setName(title);


        if (reminder != null) {
            newReminder.setUserId(reminder.getUserId());
            reminderService.removeReminder(reminder);

            reminder = newReminder;
        }

        reminderService.addReminder(newReminder);

        finish();
    }
}
