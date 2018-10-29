package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

public interface DBSavable {
    void addToDB(DatabaseReference databaseReference);
}
