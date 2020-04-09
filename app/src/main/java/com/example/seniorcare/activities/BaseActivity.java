package com.example.seniorcare.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.ContactInfo;
import com.example.seniorcare.models.LocationInfo;
import com.example.seniorcare.models.ManagedUserPair;
import com.example.seniorcare.models.Reminder;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;
import com.example.seniorcare.services.AccountService;
import com.example.seniorcare.services.ContactInfoService;
import com.example.seniorcare.services.LocationService;
import com.example.seniorcare.services.ManageUserService;
import com.example.seniorcare.services.ReminderService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;


public abstract class BaseActivity extends AppCompatActivity implements SensorEventListener {
//    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String SENIOR_CARE_PREF_NAME = "seniorCarePref";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL = 60000; // Every 60 seconds.

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value, but they may be less frequent.
     */
    private static final long FASTEST_UPDATE_INTERVAL = 30000; // Every 30 seconds

    /**
     * The max time before batched results are delivered by location services. Results may be
     * delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 5; // Every 5 minutes.

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    protected FusedLocationProviderClient mFusedLocationClient;

    protected AccountService accountService;
    protected ManageUserService managedUserService;
    protected ContactInfoService contactInfoService;
    protected ReminderService reminderService;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SqLiteLocalDbContext<User> userSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new User());
        SqLiteLocalDbContext<ContactInfo> contactInfoSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new ContactInfo());
        SqLiteLocalDbContext<UserPassCode> userPassCodeSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new UserPassCode());
        SqLiteLocalDbContext<ManagedUserPair> managedUserPairSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new ManagedUserPair());
        SqLiteLocalDbContext<Reminder> reminderSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new Reminder());

        AlarmManager alarmManager =(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        accountService = new AccountService(
                userSqLiteLocalDbContext,
                userPassCodeSqLiteLocalDbContext,
                contactInfoSqLiteLocalDbContext,
                getSharedPreferences(SENIOR_CARE_PREF_NAME, Context.MODE_PRIVATE)
        );

        managedUserService = new ManageUserService(
                managedUserPairSqLiteLocalDbContext,
                userPassCodeSqLiteLocalDbContext,
                userSqLiteLocalDbContext
        );

        contactInfoService = new ContactInfoService(
          contactInfoSqLiteLocalDbContext
        );

        reminderService = new ReminderService(
                reminderSqLiteLocalDbContext,
                alarmManager,
                getApplicationContext()
        );

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                handleMessageAction(message);
            }
        };
    }

    protected void handleMessageAction(Message message) {
    }

    protected void startLocationUpdates() {
        fetchLocation();
        initMotionDetector();
        registerMotionSensor();
    }

    protected void onFetchLocationSuccess(Location location) {

        AccountService accountService = new AccountService(
                new SqLiteLocalDbContext<>(this, new User()),
                new SqLiteLocalDbContext<>(this, new UserPassCode()),
                new SqLiteLocalDbContext<>(this, new ContactInfo()),
                getSharedPreferences(SENIOR_CARE_PREF_NAME, Context.MODE_PRIVATE)
        );
        User currentUser = accountService.getCurrentUser();

        LocationService locationService = new LocationService(
                new SqLiteLocalDbContext<>(this, new LocationInfo())
        );
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setUserId(currentUser.getUserId());
        locationService.updateLocation(locationInfo);
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> onFetchLocationSuccess(location));
    }

    @Override
    protected void onResume() {
        super.onResume();
//            startLocationUpdates();
    }

    private void stopLocationUpdates() {
        unRegisterMotionSensor();
    }


    private Sensor linearAccelerationSensor;
    private SensorManager sensorManager;
    public void initMotionDetector() {
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);;
        this.linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void registerMotionSensor() {
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterMotionSensor() {
        sensorManager.unregisterListener(this, linearAccelerationSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        if (Math.abs(values[0]) > 0.5 || Math.abs(values[1]) > 0.5 || Math.abs(values[2]) > 0.5) {
            fetchLocation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }
}
