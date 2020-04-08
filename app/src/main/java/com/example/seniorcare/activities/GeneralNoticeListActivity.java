package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorcare.R;
import com.example.seniorcare.models.Reminder;
import com.example.seniorcare.models.User;
import com.example.seniorcare.services.AccountService;

import java.util.List;

import static com.example.seniorcare.activities.Constant.SENIOR_NAME;
import static com.example.seniorcare.activities.Constant.SENIOR_USER_ID;

public class GeneralNoticeListActivity extends BaseActivity implements View.OnClickListener {

    ImageButton addNotificationButton;
    private String seniorName;
    private String seniorUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        seniorName = intent.getStringExtra(SENIOR_NAME);
        seniorUserId = intent.getStringExtra(SENIOR_USER_ID);

        initActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        initActivity();
    }

    private void initActivity() {
        try {
            setContentView(R.layout.activity_general_notice_list);

            addNotificationButton = findViewById(R.id.addNoticeImageButton);
            addNotificationButton.setOnClickListener(this);

            RecyclerView noticeListView = findViewById(R.id.noticeRecycleView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            noticeListView.setLayoutManager(layoutManager);

            User senior = accountService.getUserById(seniorUserId);
            List<Reminder> reminderList = reminderService.getReminders(senior);
            NoticeListAdapter sensorListAdapter = new NoticeListAdapter(this.getApplicationContext(), reminderList);
            noticeListView.setAdapter(sensorListAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent addNotificationIntent = new Intent(this, SetNotificationActivity.class);
        addNotificationIntent.putExtra(SENIOR_NAME, seniorName);
        addNotificationIntent.putExtra(SENIOR_USER_ID, seniorUserId);
        startActivity(addNotificationIntent);
    }
}
