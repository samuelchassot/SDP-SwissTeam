package ch.epfl.swissteam.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.location.LocationServices;

public class GoogleSignInSingleton {
    private GoogleSignInClient client_;
    private String clientUniqueID_;
    private Location currentLocation;
    private static GoogleSignInSingleton instance_;


    /**
     * if the instance of the singleton object doesn't exist, it creates it. If the clientUniqueID has not been instantiated yet, it instantiates it with the given parameter
     * @param clientUniqueID
     */
    public static void putUniqueID(String clientUniqueID){
        if(instance_ == null){
            instance_ = new GoogleSignInSingleton();
        }
        if(instance_.clientUniqueID_ == null && clientUniqueID != null){
            instance_.clientUniqueID_ = clientUniqueID;
        }
    }

    /**
     * if the instance of the singleton object doesn't exist, it creates it. If the client has not been instantiated yet, it instantiates it with the given parameter
     * @param client
     */
    public static void putGoogleSignInClient(GoogleSignInClient client){
        if(instance_ == null){
            instance_ = new GoogleSignInSingleton();
        }
        if(instance_.client_ == null && client != null){
            instance_.client_ = client;
        }
    }

    /**
     * updates the location in the user in the DB using the currentLocation attribute and the clientUniqueID attribute
     * if one of those are null, do nothing
     * if the user doesn't exist yet, do nothing
     */
    public static void updateLastLocationUserInDB(){
        if(instance_ != null){
            if(instance_.clientUniqueID_ != null && instance_.currentLocation != null){
                DBUtility.get().getUser(instance_.clientUniqueID_, (u)->{
                    if(u != null){
                        User newUser = new User(u.getGoogleId_(), u.getName_(), u.getEmail_(), u.getDescription_(), u.getCategories_(), u.getImageUrl_(), u.getRating_(),
                                instance_.getLastLocation().getLatitude(), instance_.getLastLocation().getLongitude());
                        newUser.addToDB(DBUtility.get().getDb_());

                    }
                });
            }
        }
    }

    /**
     * if the instance of the singleton object doesn't exist, it creates it. If the currentLocation has not been instantiated yet,
     * it instanties it with the current location (using the give activity)
     * @param activity
     */
    public static void putCurrentLocation(Activity activity){
        if(instance_ == null){
            instance_ = new GoogleSignInSingleton();
        }
        if(instance_.currentLocation == null){

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            else {
                LocationServices.getFusedLocationProviderClient(activity).getLastLocation()
                        .addOnSuccessListener((location) -> {
                            if(location != null ) {
                                instance_.currentLocation = new Location(location);
                                Log.i("NewLocation", location.toString());
                            }
                        });
            }
        }
    }

    public static GoogleSignInSingleton get(){
        if(instance_ == null){
            instance_ = new GoogleSignInSingleton();
        }
        return instance_;
    }

    private GoogleSignInSingleton(){
        client_ = null;
        clientUniqueID_ = null;

    }

    /**
     * Can be null
     * @return GoogleSignInClient
     */
    public GoogleSignInClient getClient(){
        return client_;
    }

    /**
     * Can be null
     * @return the clientUniqueID
     */
    public String getClientUniqueID(){
        return clientUniqueID_;
    }

    public Location getLastLocation(){
        return currentLocation;
    }


}
