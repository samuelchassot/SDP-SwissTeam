package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

/**
 * An interface for class that saves data in DB
 */
public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
}
