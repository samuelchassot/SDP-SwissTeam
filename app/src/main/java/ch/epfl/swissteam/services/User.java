package ch.epfl.swissteam.services;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a user in the database
 *
 * @author simonwicky
 */
public class User implements DBSavable {

    private String googleId_, email_, name_, description_, imageUrl_;
    private int rating_;
    private double latitude_, longitude_;
    private ArrayList<Categories> categories_;

    private ArrayList<ChatRelation> chatRelations_;

    /**
     * return the GoogleID that corresponds to a deleted user
     *
     * @return the deleted user's GoogleID
     */
    public static String getDeletedUserGoogleID() {
        return "000000000000000000000";
    }

    /**
     * return a deleted user
     *
     * @return a deleted user
     */
    public static User getDeletedUser(){
        User deletedUser = new User(getDeletedUserGoogleID(), "Deleted user",
                "", "", new ArrayList<Categories>(), "https://cdn.pixabay.com/photo/2014/03/25/15/19/cross-296507_960_720.png", 0, 0.0,0.0 );
        return deletedUser;
    }

    /**
     * Default constructor, needed for database
     */
    public User() {
        categories_ = new ArrayList<>();
        chatRelations_ = new ArrayList<>();
    }

    /**
     * Create a new user given its specificities
     *
     * @param googleID_    User's unique googleId
     * @param name_        User's name
     * @param email_       User's email
     * @param description_ User's description
     * @param categories_  User's categories of services
     * @param chatRelations_ User's chat relations
     * @param rating_      User's rating score
     * @param latitude_    User's last latitude
     * @param longitude_   User's last longitude
     */
    public User(String googleID_, String name_, String email_, String description_,
                ArrayList<Categories> categories_, ArrayList<ChatRelation> chatRelations_,
                String imageUrl_, int rating_, double latitude_, double longitude_) {
        this.googleId_ = googleID_;
        this.email_ = email_;
        this.name_ = name_;
        this.description_ = description_;
        this.imageUrl_ = imageUrl_;
        this.rating_ = rating_;
        this.categories_ = categories_ == null ? new ArrayList<>() : (ArrayList<Categories>) categories_.clone();
        this.chatRelations_ = chatRelations_ == null ? new ArrayList<>() : (ArrayList<ChatRelation>)  chatRelations_.clone();
        this.latitude_ = latitude_;
        this.longitude_ = longitude_;
    }

    /**
     * Gives the google id of the user
     *
     * @return the google id of the user
     */
    public String getGoogleId_() {
        return googleId_;
    }

    /**
     * Gives the name of the user
     *
     * @return the name of the user
     */
    public String getName_() {
        return name_;
    }

    /**
     * Gives the email of the user
     *
     * @return the email of the user
     */
    public String getEmail_() {
        return email_;
    }

    /**
     * Gives the description of the user
     *
     * @return the description of the user
     */
    public String getDescription_() {
        return description_;
    }

    /**
     * Gives the url of the image of the user
     *
     * @return the url of the image of the user
     */
    public String getImageUrl_() {
        return imageUrl_;
    }

    /**
     * Gives the rating of the user
     *
     * @return the rating of the user
     */
    public int getRating_() {
        return rating_;
    }


//    public Location getLastLocation() {
//        Location lastLocation = new Location("");
//        lastLocation.setLongitude(longitude_);
//        lastLocation.setLatitude(latitude_);
//        return lastLocation;
//    }
//    public void setLastLocation_(Location lastLocation){
//        if(lastLocation != null){
//          this.latitude_ = lastLocation.getLatitude();
//          this.longitude_ = lastLocation.getLongitude();
//        }
//    }


    /**
     * Gives the latitude of the user
     *
     * @return the latitude of the user
     */
    public double getLatitude_() {
        return latitude_;
    }

    /**
     * Gives the longitude of the user
     *
     * @return the longitude of the user
     */
    public double getLongitude_() {
        return longitude_;
    }

    /**
     * Gives the list of categories of the user if it exists, creates a new empty list otherwise
     *
     * @return the list of categories of the user
     */
    public ArrayList<Categories> getCategories_() {
        if (categories_ == null) {
            return new ArrayList<>();
        }
        return (ArrayList<Categories>) categories_.clone();
    }

    /**
     * Gives the list of chat relations of the user if it exists, creates a new empty list otherwise
     *
     * @return the list of categories of the user
     */
    public ArrayList<ChatRelation> getChatRelations_() {
        if (chatRelations_ == null) {
            return new ArrayList<>();
        }
        return (ArrayList<ChatRelation>) chatRelations_.clone();
    }

    /**
     * Add the user to a database
     *
     * @param db the database in which to add the user
     */
    public void addToDB(DatabaseReference db) {
        db.child(DBUtility.USERS).child(googleId_).setValue(this);
        if (categories_ != null) {
            for (Categories category : categories_) {
                db.child(DBUtility.CATEGORIES).child(category.toString()).child(googleId_).setValue("true");
            }
        }

        Log.e("USER", "ADDED");
    }

    @Override
    public void removeFromDB(DatabaseReference db) throws Utility.IllegalCallException {
        //remove the user's entry
        db.child(DBUtility.USERS).child(googleId_).removeValue();

        //remove user's from all categories
        List<Categories> allCat = new ArrayList<Categories>(Arrays.asList(Categories.values()));
        for (Categories c : allCat) {
            DBUtility.get().getCategory(c, cat -> {
                cat.removeUser(this);
                cat.addToDB(db);
            });
        }

        //remove the user from posts he/she made
        DBUtility.get().getUsersPosts(googleId_, posts -> {
            for (Post p : posts) {
                p.removeUser();
            }
        });

        //remove the user from all ChatRelations and delete the relation if both users are removed
        boolean removed = false;
        for (ChatRelation cr : chatRelations_) {
            if (cr.getFirstUserId_().equals(googleId_)) {
                if (cr.getSecondUserId_().equals(User.getDeletedUserGoogleID())) {
                    cr.removeFromDB(db);
                    removed = true;
                } else {
                    cr.setFirstUserId_(User.getDeletedUserGoogleID());
                }
            }
            if (cr.getSecondUserId_().equals(googleId_) && !removed) {
                if (cr.getFirstUserId_().equals(User.getDeletedUserGoogleID())) {
                    cr.removeFromDB(db);
                    removed = true;
                } else {
                    cr.setSecondUserId_(User.getDeletedUserGoogleID());
                }

            }
            if(!removed){
                //change the ID in all messages the user sent
                DBUtility.get().getAllMessagesFromChatRelation(cr.getId_(), messages->{
                    for(ChatMessage m : messages){
                        m.setUserId_(User.getDeletedUserGoogleID());
                        m.setUser_(User.getDeletedUser().getName_());
                        m.addToDB(db);
                    }
                });
                cr.addToDB(db);
            }
        }






    }

    /**
     * Add a chatRelation to the list of chatRelationId of the user and save it into the database db
     *
     * @param chatRelation the id of the chatRelation
     * @param db           reference to the database to update the user
     */
    public void addChatRelation(ChatRelation chatRelation, DatabaseReference db) {
        if (chatRelations_ == null) {
            chatRelations_ = new ArrayList<>();
        }
        chatRelations_.add(chatRelation);
        if (db != null) {
            addToDB(db);
        }
    }

    @Override
    public boolean equals(Object other) {
        return this.googleId_.equals(((User) other).getGoogleId_());
    }

    /**
     * Add a chatRelationId to the list of chatRelation of the user
     *
     * @param chatRelation the id of the chatRelation
     */
    public void addChatRelation(ChatRelation chatRelation) {
        addChatRelation(chatRelation, null);
    }

    /**
     * return the chatRelation that this user have with other if it exists already and null otherwise
     *
     * @param other the other user
     * @return the chatRelation that this user have with other if it exists already and null otherwise
     */
    public ChatRelation relationExists(User other) {
        return relationExists(other.getGoogleId_());
    }

    /**
     * return the chatRelation that this user have with otherID if it exists already and null otherwise
     *
     * @param otherId the other user's ID
     * @return the chatRelation that this user have with other if it exists already and null otherwise
     */
    public ChatRelation relationExists(String otherId) {
        if (chatRelations_ == null) return null;
        for (ChatRelation cR : chatRelations_) {
            if (cR.getFirstUserId_().compareTo(getGoogleId_()) == 0 && cR.getSecondUserId_().compareTo(otherId) == 0
                    || cR.getFirstUserId_().compareTo(otherId) == 0 && cR.getSecondUserId_().compareTo(getGoogleId_()) == 0) {
                return cR;
            }
        }
        return null;
    }

    /**
     * Increments user's rating by 1
     */
    public void upvote() {
        rating_ += 1;
    }

    /**
     * Decrements user's rating by 1
     */
    public void downvote() {
        rating_ -= 1;
    }
}
