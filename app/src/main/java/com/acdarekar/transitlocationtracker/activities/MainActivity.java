package com.acdarekar.transitlocationtracker.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acdarekar.transitlocationtracker.data.Constants;
import com.acdarekar.transitlocationtracker.R;
import com.acdarekar.transitlocationtracker.data.Route;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.acdarekar.transitlocationtracker.Services.TrackerService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        assert lm != null;
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            finish();
        }

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }

        FirebaseUser user = Constants.getUser();
        final TextView tv = findViewById(R.id.name);
        tv.setText(user.getEmail());

        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Constants.setUser(null);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        Button serviceButton = findViewById(R.id.start_tracking);

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrackerService();
            }
        });

        Button mapButton = findViewById(R.id.map);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        Button registerButton = findViewById(R.id.register_route);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);

                dialogbuilder.setTitle("Register Route");
                dialogbuilder.setView(LayoutInflater.from(MainActivity.this).inflate( R.layout.register_route, null));
                dialogbuilder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = Constants.getUser();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("buses").child(user.getUid());
                        Route route = new Route(user.getUid(), "144", "Kothrud Stand to Pune Station", 1);
                        database.setValue(route);
                        Toast.makeText(MainActivity.this, "Registered!", Toast.LENGTH_LONG).show();
                        Constants.setRoute(route);
                    }
                });

                dialogbuilder.setCancelable(true);
                dialogbuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_LONG).show();
                    }});
                AlertDialog dialog = dialogbuilder.create();

                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults){
        if (requestCode != PERMISSIONS_REQUEST || grantResults.length != 1
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        if (TrackerService.isInstanceCreated() != null) {
            TrackerService.setInstance(null);
            stopService(new Intent(this, TrackerService.class));
            Toast.makeText(this, "GPS tracking disabled", Toast.LENGTH_SHORT).show();
            Button serviceButton = findViewById(R.id.start_tracking);
            serviceButton.setText(R.string.enable_tracking);
        }else {
            TrackerService.setInstance(new TrackerService());
            startService(new Intent(this, TrackerService.class));
            Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
            Button serviceButton = findViewById(R.id.start_tracking);
            serviceButton.setText(R.string.disable_tracking);
        }
    }
}
