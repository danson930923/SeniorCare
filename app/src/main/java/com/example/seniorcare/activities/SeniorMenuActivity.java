package com.example.seniorcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorcare.R;
import com.example.seniorcare.models.User;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

public class SeniorMenuActivity extends BaseActivity implements View.OnClickListener {

    ImageButton makeCallButton;
    Button logoutButton;
    TextView tokenTextView;
    private static final int PERMISSIONS_REQUEST_CODE = 11;

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
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "Here",Toast.LENGTH_SHORT).show();
        User manager = managedUserService.getManagerUser(accountService.getCurrentUser());
        String phonenumber = contactInfoService.getPhoneNumber(manager);
        if (v.getId() == R.id.callOutButton) {
            makeCallService(phonenumber);
        } else if (v.getId() == R.id.logoutButton) {
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

//    protected void updateCurrentLocation(){
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//        double lat = location.getLatitude();
//        double lng = location.getLongitude();
//    }

}
