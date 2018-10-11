package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Categories implements DBSavable {
        IC,
        MATHS,
        GARDENING,
        COOKING,
        DAILYLIFE,
        TRANSPORTATION,
        HOUSE,
        TEACHING,
        MECHANICS;

    private ArrayList<String> users_;

    Categories(){
        users_ = new ArrayList<>();
    }

    public void addUser(User user){
        addUser(user.getGoogleId_());
    }

    public void addUser(String googleId){
        users_.add(googleId);
    }

    public void removeUser(User user){
        removeUser(user.getGoogleId_());
    }

    public void removeUser(String googleId){
        users_.remove(googleId);
    }

    public List<String> getUsers_() {
        return Collections.unmodifiableList(users_);
    }

    @Override
    public String toString(){
        switch (this){
            case IC: return "Computer";
            case MATHS: return "Maths";
            case HOUSE: return "House";
            case COOKING: return "Cooking";
            case TEACHING: return "Teaching";
            case GARDENING: return "Gardening";
            case MECHANICS: return "Mechanics";
            case DAILYLIFE: return "Daily Life";
            case TRANSPORTATION: return "Transportation";
        }
        return null;

    }

    public static Categories fromString(String category){
        switch (category){
            case "Computer" : return IC;
            case "Maths" : return MATHS;
            case "House" : return HOUSE;
            case "Cooking" : return COOKING;
            case "Teaching" : return TEACHING;
            case "Gardening" : return GARDENING;
            case "Mechanics" : return MECHANICS;
            case "Daily Life" : return DAILYLIFE;
            case "Transportation" : return TRANSPORTATION;
            default: return null;

        }
    }

    @Override
    public void addToDB(DatabaseReference db) {
        db.child(DBUtility.get().getCATEGORIES()).removeValue();
        for (String googleId : users_) {
            db.child(DBUtility.get().getCATEGORIES()).child(this.toString()).child(googleId).setValue("true");
        }
    }
}
