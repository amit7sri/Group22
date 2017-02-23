package group22.android.com.group22.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import group22.android.com.group22.model.Accelo;

import static android.content.ContentValues.TAG;

/**
 * Created by amitn on 17-02-2017.
 */

public class SensorHandler extends Service implements SensorEventListener {
    public static final String TAG = "SensorHandler";

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    int INTERVAL = 1000000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer,
                INTERVAL, new Handler());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long oldts;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

//        Accelo accelo = new Accelo( DateFormat.getDateTimeInstance().format(new Date()), x, y,z);*/

        long newts =  System.currentTimeMillis();
        if(newts - oldts >=1000) {
            DatabaseHandler hdb = new DatabaseHandler(this);
            hdb.addToTable(new Accelo(DateFormat.getDateTimeInstance().format(new Date()), event.values[0], event.values[1], event.values[2]));
            oldts = newts;
        }
        Log.d(TAG, "X Y Z values are   " +  x +" " + y +"  " + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
