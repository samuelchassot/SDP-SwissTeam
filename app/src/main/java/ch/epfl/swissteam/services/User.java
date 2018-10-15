package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Class representing a user in the database,
 * @author simonwicky
 */
public class User implements DBSavable{




    private String googleId_, email_, name_, description_;

    private ArrayList<Categories> categories_;

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
    public User(String googleID_, String name_, String email_, String description_, ArrayList<Categories> categories_) {
        this.googleId_ = googleID_;
        this.email_ = email_;
        this.name_ = name_;
        this.description_ = description_;
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

    public ArrayList<Categories> getCategories_() {
        return categories_.clone();
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
    }



}
