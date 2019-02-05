package edu.nandboolean.incaseofemergency;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.nandboolean.incaseofemergency.utils.UserLocationTracker;

public abstract class ICEActivity extends AppCompatActivity {
    protected UserLocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.locationTracker = UserLocationTracker.getLocationTracker(this);
        if (!locationTracker.checkLocationPermission(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    UserLocationTracker.PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            Set<String> locationProviders = new HashSet<>();
            Collections.addAll(locationProviders, LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER);
            locationTracker.requestLocationUpdates(this, locationProviders);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == UserLocationTracker.PERMISSION_ACCESS_FINE_LOCATION
            && permissions.length == 1
            && permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)
            && grantResults[0] == -1) {
            onFailedToObtainLocationPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected abstract void onFailedToObtainLocationPermission();
}
