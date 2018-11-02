package ch.epfl.swissteam.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

import java.util.Date;

/**
 * This singleton class contains the methods related to the location
 *
 * @author Adrian Baudat
 */
public class LocationManager {

    public final static float MAX_POST_DISTANCE = 10000000; //in meters
    public final static int M_IN_ONE_KM = 1000;
    private final static int TIME_BETWEEN_UPDATES = 10000; //in ms

    private Location currentLocation_ = null; //TODO: Maybe make this observable and remove getter, replace by onChangeListener
    private boolean isMock = false;
    private static LocationManager instance;
    private long lastUpateTime = 0;

    /**
     * Get the only instance of LocationManager;
     *
     * @return instance on LocationManager
     */
    public static LocationManager get() {
        if (instance == null) {
            instance = new LocationManager();
            return instance;
        } else return instance;
    }

    /**
     * Refreshes the LocationManager, which will fetch the current location asynchronously.
     * When it gets the location, it puts it in the DB for the user if the clientUniqueID is not null in GoogleSignSingleton
     *
     * @param activity calling activity
     */
    public void refresh(Activity activity) {
        if((lastUpateTime + TIME_BETWEEN_UPDATES) < (new Date()).getTime() && !isMock) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                lastUpateTime = (new Date()).getTime();
                String googleClientID = GoogleSignInSingleton.get().getClientUniqueID();
                LocationServices.getFusedLocationProviderClient(activity).getLastLocation().addOnSuccessListener(location -> {
                    currentLocation_ = location;
                    if (googleClientID != null) {
                        DBUtility.get().getUser(googleClientID, (u) -> {
                            if (u != null && currentLocation_ != null) {
                                User newUser = new User(u.getGoogleId_(), u.getName_(), u.getEmail_(), u.getDescription_(), u.getCategories_(), u.getImageUrl_(), u.getRating_(),
                                        currentLocation_.getLatitude(), currentLocation_.getLongitude(), u.getChatRelations_());
                                newUser.addToDB(DBUtility.get().getDb_());

                            }
                        });
                    }
                });
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
    public Location getCurrentLocation_() {
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
