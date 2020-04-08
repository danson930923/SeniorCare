package com.example.seniorcare.activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.seniorcare.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GoogleMapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MotionDetector MotionDetector;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        MotionDetector = new MotionDetector(
                getApplicationContext(),
                sensorManager,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        MotionDetector.Register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MotionDetector.UnRegister();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        super.startLocationUpdates();
    }

    @Override
    protected void onFetchLocationSuccess(Location location) {
        super.onFetchLocationSuccess(location);

        if (location != null) {
            LatLng latLng = new LatLng(
                    location.getLatitude(),
                    location.getLongitude()
            );
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

}
