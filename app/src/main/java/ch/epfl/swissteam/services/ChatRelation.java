package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRelation implements DBSavable {

    public ChatRelation(String ... userIds){
        userIds_ = userIds == null ? null : new ArrayList<> (Arrays.asList(userIds));
    }

    public ArrayList<String> getUserIds_() {
        return userIds_ == null ? null : (ArrayList<String>) userIds_.clone();
    }

    public void setUserIds_(ArrayList<String> users) {
        userIds_ = users == null ? null : (ArrayList<String>) users.clone();
    }

    private ArrayList<String> userIds_;

    @Override
    public void addToDB(DatabaseReference databaseReference) {

    }
}
