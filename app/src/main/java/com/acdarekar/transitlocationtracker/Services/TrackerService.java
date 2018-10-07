package com.acdarekar.transitlocationtracker.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.acdarekar.transitlocationtracker.data.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackerService extends Service{
    private static TrackerService instance = null;
    private String TAG = getClass().getSimpleName();
    private LocationRequest request;

    public static void setInstance(TrackerService instance) {
        TrackerService.instance = instance;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        instance = this;
        request = new LocationRequest();
        pushLocation();
    }

    private void pushLocation() {
        FirebaseUser user = Constants.getUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("buses").child(user.getUid()).child("location");


        request.setInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (instance != null){
                        if (location != null) {
                            Log.d(TAG, location.toString());
                            database.child("location").push();
                            database.child("longitude").setValue(location.getLongitude());
                            database.child("latitude").setValue(location.getLatitude());
                        } else {
                            Log.d(TAG, "location could not be retrieved");
                        }
                    }else
                        Log.d(TAG, "location Service Disabled!");

                }
            }, null);
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public static TrackerService isInstanceCreated() {
        return instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
