package com.example.seniorcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

public class SeniorMenuActivity extends AppCompatActivity implements ImageButton.OnClickListener {

    ImageButton makeCallButton;
    private static final int PERMISSIONS_REQUEST_CODE = 11;
    List<Time> noticeTimeList = new ArrayList<Time>();
    private Vibrator vibrator;
    private Handler ringPhoneHandler;
    private Runnable ringPhoneRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_menu);

        RecyclerView noticeListView = findViewById(R.id.noticeRecycleView);
        ringPhoneRunnable = getRingPhoneRunnable();
        ringPhoneHandler = new Handler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeListView.setLayoutManager(layoutManager);

        SeniorNoticeListAdapter sensorListAdapter = new SeniorNoticeListAdapter(this.getApplicationContext());
        noticeListView.setAdapter(sensorListAdapter);

        makeCallButton = findViewById(R.id.callOutButton);
        makeCallButton.setOnClickListener(this);
//        Time now = new Time(19, 29, 00);
//        noticeTimeList.add(now);
    }

    @Override
    public void onResume(){
        super.onResume();
//        Time today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//
//        if (noticeTimeList.contains(now)){
//            vibrateNotice();
//        }
    }

    @Override
    public void onClick(View v) {
        makeCallService("7788632802");
    }

    protected void vibrateNotice() {
        ringPhoneHandler.postDelayed(ringPhoneRunnable, 500);
    }

    private Runnable getRingPhoneRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (!vibrator.hasVibrator()) {
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(60000);
                }
            }
        };
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
}
