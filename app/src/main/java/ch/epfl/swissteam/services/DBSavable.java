package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

/**
 * An interface for class that saves data in DB
 */
public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
    default void removeFromDB(DatabaseReference databaseReference) throws Utility.IllegalCallException {
        throw new Utility.IllegalCallException("This method needs to be defined to be call.");
    }
}
