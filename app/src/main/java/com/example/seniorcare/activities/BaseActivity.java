package com.example.seniorcare.activities;

import android.Manifest;
import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


public abstract class BaseActivity extends AppCompatActivity {
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
    private LocationCallback locationCallback;


    protected AccountService accountService;
    protected ManageUserService managedUserService;
    protected ContactInfoService contactInfoService;
    protected ReminderService reminderService;
    protected PlacesClient mGeoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context self = this;

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

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    AccountService accountService = new AccountService(
                            new SqLiteLocalDbContext<>(self, new User()),
                            new SqLiteLocalDbContext<>(self, new UserPassCode()),
                            new SqLiteLocalDbContext<>(self, new ContactInfo()),
                            getSharedPreferences(SENIOR_CARE_PREF_NAME, Context.MODE_PRIVATE)
                    );
                    User currentUser = accountService.getCurrentUser();

                    LocationService locationService = new LocationService(
                            new SqLiteLocalDbContext<>(self, new LocationInfo())
                    );
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setUserId(currentUser.getUserId());
                    locationService.updateLocation(locationInfo);
                }
            }
        };

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL)
            .setMaxWaitTime(MAX_WAIT_TIME);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

    }

    protected void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    protected void onFetchLocationSuccess(Location location) {
        SqLiteLocalDbContext<LocationInfo> locationInfoSqLiteLocalDbContext = new SqLiteLocalDbContext<>(this, new LocationInfo());
        User currentUser = accountService.getCurrentUser();
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setUserId(currentUser.getUserId());
        locationInfo.setLocationInfo(location);
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//            startLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
