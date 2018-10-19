package ch.epfl.swissteam.services;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a user in the database,
 * @author simonwicky
 */
public class User implements DBSavable{




    private String googleId_, email_, name_, description_, imageUrl_;

    private ArrayList<Categories> categories_;

    private ArrayList<ChatRelation> chatRelations_;

    /**
     * Default constructor, needed for database
     */
    public User(){

    }

    /**
     * Create a new user given its specificities

     * @param googleID_ User's unique googleId
     * @param name_ User's name
     * @param email_ User's email
     * @param description_ User's description
     * @param categories_ User's categories of services
     */
    public User(String googleID_, String name_, String email_, String description_, ArrayList<Categories> categories_, String imageUrl_) {
        this.googleId_ = googleID_;
        this.email_ = email_;
        this.name_ = name_;
        this.description_ = description_;
        this.imageUrl_ = imageUrl_;
        this.categories_ = categories_ == null ? new ArrayList<Categories>() : (ArrayList<Categories>) categories_.clone();
    }

    public String getGoogleId_() { return googleId_; }

    public String getName_() {
        return name_;
    }

    public String getEmail_() {
        return email_;
    }

    public String getDescription_() {
        return description_;
    }

    public String getImageUrl_() { return imageUrl_; }

    public ArrayList<Categories> getCategories_() {
        if(categories_ == null){
            return new ArrayList<>();
        }
        return (ArrayList<Categories>) categories_.clone();
    }

    public ArrayList<ChatRelation> getChatRelations_() {
        return chatRelations_ == null ? null : (ArrayList<ChatRelation>) chatRelations_.clone();//Collections.unmodifiableList(chatRelations_);
    }

    /**
     * Add the user to a database
     * @param db the database in which to add the user
     */
    public void addToDB(DatabaseReference db){
        db.child(DBUtility.USERS).child(googleId_).setValue(this);
        if(categories_ != null) {
            for (Categories category : categories_) {
                db.child(DBUtility.CATEGORIES).child(category.toString()).child(googleId_).setValue("true");
            }
        }

        Log.e("USER", "ADDED");
    }

    /**
     * Add a chatRelation to the list of chatRelationId of the user and save it into the database
     * db
     * @param chatRelation the id of the chatRelation
     * @param db reference to the database to update the user
     */
    public void addChatRelation(ChatRelation chatRelation, DatabaseReference db){
        if(chatRelations_ == null) {
            chatRelations_ = new ArrayList<>();}
        chatRelations_.add(chatRelation);
        if(db != null){
            addToDB(db);
        }
    }

    /**
     * Add a chatRelationId to the list of chatRelation of the user
     * db
     * @param chatRelation the id of the chatRelation
     */
    public void addChatRelation(ChatRelation chatRelation){
        addChatRelation(chatRelation, null);
    }

    public ChatRelation relationExists(User other){
        if(chatRelations_ == null) return null;
        for(ChatRelation cR : chatRelations_){
            if(cR.getFirstUserId_().compareTo(googleId_) == 0 && cR.getSecondUserId_().compareTo(other.googleId_) == 0
                    || cR.getFirstUserId_().compareTo(other.googleId_) == 0 && cR.getSecondUserId_().compareTo(googleId_) == 0){
                return cR;
            }
        }
        return null;
    }
}
