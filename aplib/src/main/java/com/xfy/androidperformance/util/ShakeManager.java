package com.xfy.androidperformance.util;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class ShakeManager implements SensorEventListener{
    private static ShakeManager instance;

    private SensorManager sensorManager;
    private Sensor sensor;
    private ShakeAdapter shakeAdapter = new DefaultShakeAdapter(20);
    private OnShakeListener onShakeListener;
    private long lastShakeTime;

    public static ShakeManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ShakeManager.class) {
                if (instance == null) {
                    instance = new ShakeManager(context);
                }
            }
        }
        return instance;
    }

    private ShakeManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
        if (sensorManager == null)
            return;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null)
            return;
    }

    public void setShakeAdapter(@NonNull ShakeAdapter shakeAdapter) {
        if (shakeAdapter == null)
            throw new NullPointerException();
        this.shakeAdapter = shakeAdapter;
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.onShakeListener = onShakeListener;
    }

    public void startListening() {
        if (sensorManager != null && sensor != null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopListening() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public synchronized static void release() {
        if (instance != null) {
            instance.stopListening();
            instance.sensorManager = null;
            instance.sensor = null;
            instance.shakeAdapter = null;
            instance.onShakeListener = null;
        }
        instance = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (onShakeListener == null || shakeAdapter == null)
            return;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if (shakeAdapter.onChanged(x, y, z)) {
                long now = System.currentTimeMillis();
                if (lastShakeTime == 0 || lastShakeTime + onShakeListener.getNextShakeDelay() <= now) {
                    onShakeListener.onShake();
                    lastShakeTime = now;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface ShakeAdapter {
        boolean onChanged(float x, float y, float z);
    }

    public interface OnShakeListener {
        void onShake();
        long getNextShakeDelay();
    }
}
