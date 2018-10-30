package ch.epfl.swissteam.services;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleSignInSingleton {
    private GoogleSignInClient client_;
    private String clientUniqueID_;
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



}
