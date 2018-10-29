package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

/**
 * TODO : Short Explaination
 */
public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
}
