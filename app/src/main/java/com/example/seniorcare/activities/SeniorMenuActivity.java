package com.example.seniorcare.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorcare.R;
import com.example.seniorcare.models.Reminder;
import com.example.seniorcare.models.User;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SeniorMenuActivity extends BaseActivity implements View.OnClickListener {
    private final static int REMINDER_TIME_REACH = 101;

    ImageButton makeCallButton;
    Button logoutButton;
    TextView tokenTextView;
    private static final int PERMISSIONS_REQUEST_CODE = 11;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_menu);

        tokenTextView = findViewById(R.id.tokenTextView);
        tokenTextView.setText(accountService.getCurrentUserPassCode());

        RecyclerView noticeListView = findViewById(R.id.noticeRecycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeListView.setLayoutManager(layoutManager);

        SeniorNoticeListAdapter sensorListAdapter = new SeniorNoticeListAdapter(
            this.getApplicationContext(),
            reminderService.getReminders(accountService.getCurrentUser())
        );
        noticeListView.setAdapter(sensorListAdapter);

        makeCallButton = findViewById(R.id.callOutButton);
        makeCallButton.setOnClickListener(this);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        scheduleReminderChecker();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "Here",Toast.LENGTH_SHORT).show();
        if (v.getId() == R.id.callOutButton) {
            User manager = managedUserService.getManagerUser(accountService.getCurrentUser());
            if (manager == null) {
                return;
            }
            String phonenumber = contactInfoService.getPhoneNumber(manager);
            makeCallService(phonenumber);
        } else if (v.getId() == R.id.logoutButton) {
            timer.cancel();
            accountService.logout();
            finish();
        }
    }

    protected void makeCallService(String number){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Please make the permission for phone call",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CODE);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "your number"));
            startActivity(callIntent);
        }
    }

    private void scheduleReminderChecker() {
        timer.cancel();
        timer = new Timer(true);
        timer.schedule(
                getScheduleReminderTimerTask(),
                (60 - DateTime.now().getSecondOfDay() % 60) % 60,
                1000 * 60
        );
    }

    private void stopReminderChecker() {
        timer.cancel();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        stopReminderChecker();
    }

    private TimerTask getScheduleReminderTimerTask() {
        Context self = this;
        return new TimerTask() {
            @Override
            public void run() {
                List<Reminder> reminderList = reminderService.getReminders(accountService.getCurrentUser());
                reminderList.forEach(reminder -> {
                    DateTime reminderTime = new DateTime(reminder.getTime());
                    DateTime currentTime = DateTime.now();
                    if (currentTime.getMinuteOfDay() == reminderTime.getMinuteOfDay()) {
                        handler.sendMessage(handler.obtainMessage(REMINDER_TIME_REACH, reminder));
                    }
                });
            }
        };
    }

    @Override
    protected void handleMessageAction(Message message) {
        switch (message.what) {
            case REMINDER_TIME_REACH:
                reminderAction((Reminder) message.obj);
                break;
        }
    }

    private void reminderAction(Reminder reminder) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        Toast.makeText(this, reminder.getTitle(), Toast.LENGTH_SHORT).show();
    }

//    protected void updateCurrentLocation(){
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//        double lat = location.getLatitude();
//        double lng = location.getLongitude();
//    }

}
