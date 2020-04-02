package com.example.seniorcare;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class MotionDetector implements SensorEventListener {

    private Sensor linearAccelerationSensor;
    private SensorManager sensorManager;
    private Context context;

    public MotionDetector(Context context, SensorManager sensorManager, Sensor linearAccelerationSensor) {
        this.sensorManager = sensorManager;
        this.linearAccelerationSensor = linearAccelerationSensor;
        this.context = context;
    }

    public void Register() {
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void UnRegister() {
        sensorManager.unregisterListener(this, linearAccelerationSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        if (Math.abs(values[0]) > 0.5 || Math.abs(values[1]) > 0.5 || Math.abs(values[2]) > 0.5) {
            Toast.makeText(context.getApplicationContext(),"Moving",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }
}
