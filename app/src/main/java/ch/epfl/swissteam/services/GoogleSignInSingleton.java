package ch.epfl.swissteam.services;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/**
 * TODO : Explain class
 */
public class GoogleSignInSingleton {

    private static GoogleSignInSingleton instance_;
    private GoogleSignInClient client_;
    private String clientUniqueID_;

    private GoogleSignInSingleton() {
        client_ = null;
        clientUniqueID_ = null;

    }

    /**
     * Creates the instance of the singleton object if it doesn't already exists.
     * Instantiates the clientUniqueID, if it wasn't done yet.
     *
     * @param clientUniqueID the unique id of the client
     */
    public static void putUniqueID(String clientUniqueID) {
        if (instance_ == null) {
            instance_ = new GoogleSignInSingleton();
        }
        if(instance_.clientUniqueID_ == null && clientUniqueID != null){
            instance_.clientUniqueID_ = clientUniqueID;
        }
    }

    /**
     * Creates the instance of the singleton object if it doesn't already exists.
     * Instantiates the client, if it wasn't done yet.
     *
     * @param client the client
     */
    public static void putGoogleSignInClient(GoogleSignInClient client) {
        if (instance_ == null) {
            instance_ = new GoogleSignInSingleton();
        }
        if(instance_.client_ == null && client != null){
            instance_.client_ = client;
        }
    }

    /**
     * Gives the GoogleSignIn instance if it exists, otherwise creates it before.
     *
     * @return the GoogleSignIn singleton
     */
    public static GoogleSignInSingleton get() {
        if (instance_ == null) {
            instance_ = new GoogleSignInSingleton();
        }
        return instance_;
    }

    /**
     * Gives the GoogleSignIn client (can be null)
     *
     * @return GoogleSignInClient or null if it doesn't exist
     */
    public GoogleSignInClient getClient() {
        return client_;
    }

    /**
     * Gives the GoogleSignIn clientUniqueID (can be null)
     *
     * @return the clientUniqueID
     */
    public String getClientUniqueID() {
        return clientUniqueID_;
    }



}
