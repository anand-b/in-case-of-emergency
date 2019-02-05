package edu.nandboolean.incaseofemergency.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import java.util.Set;

public class UserLocationTracker implements LocationListener {
    private static UserLocationTracker tracker = null;

    private LocationManager locationManager;
    private Location lastKnownLocation;

    public static final int PERMISSION_ACCESS_FINE_LOCATION = 103;
    public static final int MIN_TIME_BETWEEN_UPDATES_IN_MILLIS = 1000;
    public static final int MIN_DISTANCE_BETWEEN_UPDATES_IN_MILLIS = 103;

    private UserLocationTracker(Context context) {
        this.lastKnownLocation = null;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public synchronized static UserLocationTracker getLocationTracker(Context context) {
        if (tracker == null) {
            tracker = new UserLocationTracker(context);
        }
        return tracker;
    }

    public boolean checkLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationUpdates(Context context, Set<String> locationProviders) {
        if (checkLocationPermission(context)) {
            for (String provider : locationProviders) {
                this.locationManager.requestLocationUpdates(provider, MIN_TIME_BETWEEN_UPDATES_IN_MILLIS, MIN_DISTANCE_BETWEEN_UPDATES_IN_MILLIS, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLastKnownLocation() {
        return (this.lastKnownLocation == null) ? null : new Location(this.lastKnownLocation);
    }
}
