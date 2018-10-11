package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatRelation implements DBSavable {

    public ArrayList<String> getUsers_() {
        return (ArrayList<String>) users_.clone();
    }

    private ArrayList<String> users_;

    @Override
    public void addToDB(DatabaseReference databaseReference) {

    }
}
