package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a user in the database,
 * @author simonwicky
 */
public class User implements DBSavable{




    private String googleId_, email_, name_, surname_, description_;

    private ArrayList<String> categories_;

    private ArrayList<ChatRelation> chatRelations_;

    /**
     * Default constructor, needed for database
     */
    public User(){

    }

    /**
     * Create a new user given its specificities
     * @param googleID User's unique googleId
     * @param name User's name
     * @param surname User's surname
     * @param email User's email
     * @param description User's description
     * @param categories User's categories of services
     */
    public User(String googleID, String name, String surname, String email, String description, ArrayList<String> categories) {
        this.googleId_ = googleID;
        this.email_ = email;
        this.name_ = name;
        this.surname_ = surname;
        this.email_ = email;
        this.description_ = description;
        this.categories_ = categories == null ? null : (ArrayList<String>) categories.clone();
    }

    public String getGoogleId_() { return googleId_; }

    public String getName_() {
        return name_;
    }

    public String getEmail_() {
        return email_;
    }

    public String getSurname_() {
        return surname_;
    }

    public String getDescription_() {
        return description_;
    }

    public ArrayList<String> getCategories_() {
        return categories_ == null? null : (ArrayList<String>) categories_.clone();
    }

    public List<ChatRelation> getChatRelations_() {
        return Collections.unmodifiableList(chatRelations_);
    }

    /**
     * Add the user to a database
     * @param db the database in which to add the user
     */
    public void addToDB(DatabaseReference db){
        db.child("Users").child(googleId_).setValue(this);
        for (String category : categories_){
            db.child("Categories").child(category).child(googleId_).setValue("true");
        }
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
        for(ChatRelation cR : chatRelations_){
            if(cR.getFirstUserId_() == googleId_ && cR.getSecondUserId_() == other.googleId_ ||
                    cR.getFirstUserId_() == other.googleId_ && cR.getSecondUserId_() == googleId_){
                return cR;
            }
        }
        return null;
    }

}
