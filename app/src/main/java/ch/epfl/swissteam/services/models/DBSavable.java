package ch.epfl.swissteam.services.models;

import com.google.firebase.database.DatabaseReference;

import ch.epfl.swissteam.services.utils.Utils;

/**
 * An interface for class that saves data in DB
 */
public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
    /**
     * remove this DBSavable with id child from the database databaseReference
     * @param databaseReference the reference to the database
     * @throws Utils.IllegalCallException
     */
    default void removeFromDB(DatabaseReference databaseReference) throws Utils.IllegalCallException {
        throw new Utils.IllegalCallException("This method needs to be defined to be call.");
    }

    /**
     * remove the child of this DBSavable with id child from the database databaseReference
     * @param databaseReference the reference to the database
     * @param child the child to remove
     * @throws Utils.IllegalCallException
     */
    default void removeFromDB(DatabaseReference databaseReference, String child) throws Utils.IllegalCallException {
        if(databaseReference != null && child != null)
            removeFromDB(databaseReference.child(child));
        else
            removeFromDB(databaseReference);
    }
}
