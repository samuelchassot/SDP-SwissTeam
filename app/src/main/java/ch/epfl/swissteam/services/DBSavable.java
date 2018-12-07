package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

/**
 * An interface for class that saves data in DB
 */
public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
    /**
     * remove this DBSavable with id child from the database databaseReference
     * @param databaseReference the reference to the database
     * @throws Utility.IllegalCallException
     */
    default void removeFromDB(DatabaseReference databaseReference) throws Utility.IllegalCallException {
        throw new Utility.IllegalCallException("This method needs to be defined to be call.");
    }

    /**
     * remove the child of this DBSavable with id child from the database databaseReference
     * @param databaseReference the reference to the database
     * @param child the child to remove
     * @throws Utility.IllegalCallException
     */
    default void removeFromDB(DatabaseReference databaseReference, String child) throws Utility.IllegalCallException {
        if(databaseReference != null && child != null)
            removeFromDB(databaseReference.child(child));
        else
            removeFromDB(databaseReference);
    }
}
