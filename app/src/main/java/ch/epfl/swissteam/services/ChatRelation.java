package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRelation implements DBSavable {

    public ChatRelation(String ... userIds){
        userIds_ = userIds == null ? null : new ArrayList<> (Arrays.asList(userIds));
    }

    public ChatRelation(){}

    public ArrayList<String> getUserIds_() {
        return userIds_ == null ? null : (ArrayList<String>) userIds_.clone();
    }

    public String getId_() {
        return id_;
    }

    public void setUserIds_(ArrayList<String> users) {
        userIds_ = users == null ? null : (ArrayList<String>) users.clone();
    }

    public void setId_(String id) {
        this.id_ = id;
    }

    private ArrayList<String> userIds_;
    private String id_;

    @Override
    public void addToDB(DatabaseReference databaseReference) {

    }
}
