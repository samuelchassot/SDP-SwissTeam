package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a user in the database,
 * @author simonwicky
 */
public class User implements DBSavable{




    private String username_, name_, surname_, description_;
    private ArrayList<String> categories_;

    /**
     * Default constructor, needed for database
     */
    public User(){

    }

    /**
     * Create a new user given its specificities
     * @param username_ User's unique username
     * @param name_ User's name
     * @param surname_ User's surname
     * @param description_ User's description
     * @param categories_ User's categories of services
     */
    public User(String username_, String name_, String surname_, String description_, ArrayList<String> categories_) {
        this.username_ = username_;
        this.name_ = name_;
        this.surname_ = surname_;
        this.description_ = description_;
        this.categories_ = (ArrayList) categories_.clone();
    }


    public String getUsername_() {
        return username_;
    }
    public String getName_() {
        return name_;
    }

    public String getSurname_() {
        return surname_;
    }

    public String getDescription_() {
        return description_;
    }

    public List<String> getCategories_() {
        return (List) categories_.clone();
    }

    /**
     * Add the user to a database
     * @param db the database in which to add the user
     */
    public void addToDB(DatabaseReference db){
        db.child("Users").child(username_).setValue(this);
        for (String category : categories_){
            db.child("Categories").child(category).child(username_).setValue("true");
        }
    }



}
