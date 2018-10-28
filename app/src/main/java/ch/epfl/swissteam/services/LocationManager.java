package ch.epfl.swissteam.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

/**
 * This singleton class contains the methods related to the location
 *
 * @author Adrian Baudat
 */
public class LocationManager {

    private Location currentLocation_;
    private boolean isMock = false;
    private static LocationManager instance;

    /**
     * Get the only instance of LocationManager;
     *
     * @return instance on LocationManager
     */
    public static LocationManager get() {
        if(instance == null) {
            instance = new LocationManager();
            return instance;
        }
        else return instance;
    }

    /**
     * Initialize the LocationManager, which will fetch the current location asynchronously.
     *
     * @param activity calling activity
     */
    public void initialize(Activity activity) {
        if(!isMock) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                LocationServices.getFusedLocationProviderClient(activity).getLastLocation().addOnSuccessListener(location -> currentLocation_ = location);
            }
        }
    }

    /**
     * Set the LocationManager to a mocked state for testing
     */
    public void setMock() {
        isMock = true;
        currentLocation_ = new Location("");
        currentLocation_.setLongitude(0);
        currentLocation_.setLatitude(0);
    }

    /**
     * Reset the LocationManager to an unmocked state.
     */
    public void unsetMock() {
        isMock = false;
    }

    /**
     * Returns the current location. This location can be null if the Manager is not initialized
     * or the user turned off location services!
     *
     * @return current location
     */
    public Location getCurrentLocation_(){
        return currentLocation_;
    }

    /**
     * Returns a location at longitude 0 and latitude 0.
     *
     * @return 0',0' location
     */
    public Location getZeroLocation() {
        Location zero = new Location("");
        zero.setLongitude(0);
        zero.setLatitude(0);
        return zero;
    }
}