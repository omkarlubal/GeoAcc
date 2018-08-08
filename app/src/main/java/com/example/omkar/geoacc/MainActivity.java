package com.example.omkar.geoacc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private long INDEX = 0;
    private float lat=0,lon=0;
    private String LAST_STRING,LAST_SUBSTRING;
    private boolean LAST_ITEM_FLAG = true;
    private TextView x_tv,y_tv,z_tv,gps_tv;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float x,y,z;
    private LocationManager mLocationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public Location location_pass = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
    DatabaseReference rootRef;
    ModelClass modelClass;

    private final LocationListener mLocationListener = new LocationListener() {                     //GET LOCATION HERE
        @Override
        public void onLocationChanged(final Location location) {
            Log.w("GPS: ","SENSOR CHANGED!!!!!");
            gps_tv.setText("IDLE: "+location.getLatitude()+" "+location.getLongitude());
            location_pass = location;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.w("MYAPP","GPS is Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {Log.w("MYAPP","GPS is Disabled");}
    };

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {                                    //CATCH ACCELEROMETER CHANGESD

            x=event.values[0];
            y=event.values[1];
            z=event.values[2];

            if(x>8.5){                      //HORIZONTAL POSITION
                if(y>1.5 || z>1.5){
                    GetLocationUpdates();
                    if (LAST_ITEM_FLAG==true) {
                        GetLastItem();
                        LAST_ITEM_FLAG=false;
                    }
                    Log.w("GPS:","Entered");
                    if (location_pass!=null) {

                        String format = simpleDateFormat.format(new Date());
                        gps_tv.setText("Changed : "+location_pass.getLatitude()+" "+location_pass.getLongitude());

                        lat = (float)location_pass.getLatitude();
                        lon = (float) location_pass.getLongitude();
                        modelClass = new ModelClass(lat,lon,x,y,z,format);
                        INDEX++;
                        rootRef.child("DEMO").child(Long.toString(INDEX)).setValue(modelClass);
                    }

                }
            }

            if(y>8.5){                      //STANDING POSITION
                if(x>1.5 || z>1.5){
                    GetLocationUpdates();
                    if (LAST_ITEM_FLAG==true) {
                        GetLastItem();
                        LAST_ITEM_FLAG=false;
                    }
                    if (location_pass!=null) {
                        String format = simpleDateFormat.format(new Date());
                        gps_tv.setText("Changed : "+location_pass.getLatitude()+" "+location_pass.getLongitude());

                        lat = (float)location_pass.getLatitude();
                        lon = (float) location_pass.getLongitude();
                        modelClass = new ModelClass(lat,lon,x,y,z,format);
                        INDEX++;
                        rootRef.child("DEMO").child(Long.toString(INDEX)).setValue(modelClass);
                    }

                }
            }

            if(z>8.5){                      //SLEEPING POSITION
                if(y>1.5 || x>1.5){
                    GetLocationUpdates();
                    if (LAST_ITEM_FLAG==true) {
                        GetLastItem();
                        LAST_ITEM_FLAG=false;
                    }
                    if (location_pass!=null) {
                        String format = simpleDateFormat.format(new Date());
                        gps_tv.setText("Changed : "+location_pass.getLatitude()+" "+location_pass.getLongitude());

                        lat = (float)location_pass.getLatitude();
                        lon = (float) location_pass.getLongitude();
                        modelClass = new ModelClass(lat,lon,x,y,z,format);
                        INDEX++;
                        rootRef.child("DEMO").child(Long.toString(INDEX)).setValue(modelClass);
                    }

                }
            }
            x_tv.setText("X "+Float.toString(x));
            y_tv.setText("Y "+Float.toString(y));
            z_tv.setText("Z "+Float.toString(z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public boolean checkLocationPermission() {                                                                        //CHECK FOR PERMISSION
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }else{
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x_tv = findViewById(R.id.x_tv);
        y_tv = findViewById(R.id.y_tv);
        z_tv = findViewById(R.id.z_tv);
        gps_tv = findViewById(R.id.gps_tv);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);                                          //ACCELEROMETER DEFINED
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorEventListener,mAccelerometer,SensorManager.SENSOR_DELAY_UI);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);                      //GPS DEFINED
        rootRef = FirebaseDatabase.getInstance().getReference();
        checkLocationPermission();

    }

    private void GetLocationUpdates(){                                                                      //LOCATION UPDATES
        checkLocationPermission();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                0, mLocationListener);
    }

    private void GetLastItem(){
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = db.child("DEMO").orderByKey().limitToLast(1);

        Toast.makeText(MainActivity.this,lastQuery.getRef().toString(),Toast.LENGTH_SHORT).show();
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              LAST_STRING=dataSnapshot.getValue().toString();
              LAST_SUBSTRING=LAST_STRING.substring(LAST_STRING.indexOf('{')+1,LAST_STRING.indexOf('='));
              INDEX = Long.parseLong(LAST_SUBSTRING);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}
