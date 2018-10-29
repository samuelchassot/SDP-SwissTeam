package ch.epfl.swissteam.services;


/**
 * Callback interface, to do actions with data coming from Firebase
 * @param <U> The type of data returned
 */
public interface MyCallBack<U> {
    void onCallBack(U value);
}
