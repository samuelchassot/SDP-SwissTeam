package ch.epfl.swissteam.services;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a user in the database
 *
 * @author simonwicky
 */
public class User implements DBSavable {

    public static String DELETED_USER_NAME = "Deleted user";
    public static String DELETED_USER_IMG_URL = "https://cdn.pixabay.com/photo/2014/03/25/15/19/cross-296507_960_720.png";

    private String googleId_, email_, name_, description_, imageUrl_;
    private int rating_;
    private double latitude_, longitude_;
    private ArrayList<Categories> categories_;
    private HashMap<String, ArrayList<String>> keyWords_;
    private ArrayList<String> upvotes_;
    private ArrayList<String> downvotes_;

    private ArrayList<ChatRelation> chatRelations_;

    public enum Vote{
        UPVOTE,
        DOWNVOTE
    }

    public static int RATING_[] = {-2,-1,0,1,2};

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
    public static User getDeletedUser() {
        return new User(getDeletedUserGoogleID(), DELETED_USER_NAME,
                "", "", new ArrayList<Categories>(), new ArrayList<ChatRelation>(),
                DELETED_USER_IMG_URL, 0, 0.0, 0.0, new ArrayList<String>(),
                new ArrayList<String>());
    }

    /**
     * Default constructor, needed for database
     */
    public User() {
        categories_ = new ArrayList<>();
        chatRelations_ = new ArrayList<>();
        keyWords_ = new HashMap<>();
        downvotes_ = new ArrayList<>();
        upvotes_ = new ArrayList<>();
    }

    /**
     * Create a new user given its specificities
     *
     * @param googleID_      User's unique googleId
     * @param name_          User's name
     * @param email_         User's email
     * @param description_   User's description
     * @param categories_    User's categories of services
     * @param chatRelations_ User's chat relations
     * @param rating_        User's rating score
     * @param latitude_      User's last latitude
     * @param longitude_     User's last longitude
     */
    @Deprecated
    public User(String googleID_, String name_, String email_, String description_,
                ArrayList<Categories> categories_, ArrayList<ChatRelation> chatRelations_,
                String imageUrl_, int rating_, double latitude_, double longitude_,
                ArrayList<String> upvotes_, ArrayList<String> downvotes_) {
        this.googleId_ = googleID_;
        this.email_ = email_;
        this.name_ = name_;
        this.description_ = description_;
        this.imageUrl_ = imageUrl_;
        this.rating_ = rating_;
        this.categories_ = categories_ == null ? new ArrayList<>() : (ArrayList<Categories>) categories_.clone();
        this.chatRelations_ = chatRelations_ == null ? new ArrayList<>() : (ArrayList<ChatRelation>) chatRelations_.clone();
        this.upvotes_ = upvotes_ == null ? new ArrayList<String>() : (ArrayList<String>) upvotes_.clone();
        this.downvotes_ = downvotes_ == null ? new ArrayList<String>() : (ArrayList<String>) upvotes_.clone();
        this.latitude_ = latitude_;
        this.longitude_ = longitude_;
    }

    /**
     * Create a new user given its specificities
     *
     * @param googleID_      User's unique googleId
     * @param name_          User's name
     * @param email_         User's email
     * @param description_   User's description
     * @param categories_    User's categories of services
     * @param keyWords_      User's keywords for each Categories
     * @param chatRelations_ User's chat relations
     * @param rating_        User's rating score
     * @param latitude_      User's last latitude
     * @param longitude_     User's last longitude
     */

    public User(String googleID_, String name_, String email_, String description_,
                ArrayList<Categories> categories_,
                HashMap<String, ArrayList<String>> keyWords_,
                ArrayList<ChatRelation> chatRelations_,
                String imageUrl_, int rating_, double latitude_, double longitude_,
                ArrayList<String> upvotes_, ArrayList<String> downvotes_) {
        this.googleId_ = googleID_;
        this.email_ = email_;
        this.name_ = name_;
        this.description_ = description_;
        this.imageUrl_ = imageUrl_;
        this.rating_ = rating_;
        this.categories_ = categories_ == null ? new ArrayList<>() : (ArrayList<Categories>) categories_.clone();

        //keywords are stored in lowercase to simplify the comparison when searching for services
        HashMap<String, ArrayList<String>> lowercaseKeywords = new HashMap<>();
        if(keyWords_ != null) {
            ArrayList<String> kwList;
            for (String key : keyWords_.keySet()) {
                kwList = new ArrayList<>();
                for (String k : keyWords_.get(key)) {
                    kwList.add(k.toLowerCase());
                }
                lowercaseKeywords.put(key, kwList);
            }
        }
        this.keyWords_ = lowercaseKeywords;
        this.chatRelations_ = chatRelations_ == null ? new ArrayList<>() : (ArrayList<ChatRelation>) chatRelations_.clone();
        this.upvotes_ = upvotes_ == null ? new ArrayList<String>() : (ArrayList<String>) upvotes_.clone();
        this.downvotes_ = downvotes_ == null ? new ArrayList<String>() : (ArrayList<String>) upvotes_.clone();
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
     * Gives the Map of keywords for each categories the User has in his Categories
     * If the USer hadn't put any keywords for a given categories, it will not appear in the Map
     *
     * @return the map of keywords for each categories
     */
    public HashMap<String, ArrayList<String>> getKeyWords_() {
        if(keyWords_ != null){
            return (HashMap<String, ArrayList<String>>) keyWords_.clone();
        }
        return new HashMap<>();




    }

    /**
     * Gives the List<String> of keywords the user has for the given Categories
     *
     * @param c
     * @return ArrayList<String> of keyWords (can be empty)
     */
    public ArrayList<String> getKeyWords(Categories c) {
        ArrayList<String> kWords = new ArrayList<>();
        if (keyWords_.containsKey(c.toString())) {
            kWords.addAll(keyWords_.get(c.toString()));
        }
        return kWords;
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
     * Gives the list of users who upvoted this user
     *
     * @return the list of upvotes of the user
     */
    public ArrayList<String> getUpvotes_() {
        if (upvotes_ == null) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) upvotes_.clone();
    }

    /**
     * Gives the list of users who downvoted this user
     *
     * @return the list of downvotes of the user
     */
    public ArrayList<String> getDownvotes_() {
        if (downvotes_ == null) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) downvotes_.clone();
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

        Log.i("USER", "ADDED");
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
            if (!removed) {
                //change the ID in all messages the user sent
                DBUtility.get().getAllMessagesFromChatRelation(cr.getId_(), messages -> {
                    for (ChatMessage m : messages) {
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
     * @param chatRelation the chatRelation to add
     * @param db           reference to the database to update the user
     */
    public void addChatRelation(ChatRelation chatRelation, DatabaseReference db) {
        if (chatRelations_ == null) {
            chatRelations_ = new ArrayList<>();
        }

        if (!chatRelations_.contains(chatRelation)) chatRelations_.add(chatRelation);

        if (db != null) {
            addToDB(db);
        }
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
     * Remove a relation from the list of chatRelation of this user and save the user in the DB if db
     * is not null
     *
     * @param chatRelation the chatRelation to remove
     * @param db           reference to the database to update the user
     */
    public void removeChatRelation(ChatRelation chatRelation, DatabaseReference db) {
        if (chatRelations_ != null) chatRelations_.remove(chatRelation);

        if (db != null) {
            addToDB(db);
        }
    }

    /**
     * Remove a relation from the list of chatRelation of this user
     *
     * @param chatRelation the chatRelation to remove
     */
    public void removeChatRelation(ChatRelation chatRelation) {
        removeChatRelation(chatRelation, null);
    }

    @Override
    public boolean equals(Object other) {
        return this.googleId_.equals(((User) other).getGoogleId_());
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

    public void vote(Vote vote, User user) {
        if (vote == Vote.UPVOTE) {
            this.upvote(user);
        } else if (vote == Vote.DOWNVOTE) {
            this.downvote(user);
        }
        this.addToDB(DBUtility.get().getDb_());

    }

    private void upvote(User user) {
        //If already upvoted, remove it
        if (upvotes_.contains(user.getGoogleId_())) {
            upvotes_.remove(user.getGoogleId_());
            rating_ -= 1;
            return;
        }

        //if downvoted, correct the vote
        if (downvotes_.contains(user.getGoogleId_())) {
            downvotes_.remove(user.getGoogleId_());
            //one downvote less
            rating_ += 1;
        }
        upvotes_.add(user.googleId_);
        //one upvote more
        rating_ += 1;
        return;
    }


    private void downvote(User user) {
        if (downvotes_.contains(user.getGoogleId_())) {
            downvotes_.remove(user.getGoogleId_());
            rating_ += 1;
            return;
        }

        if (upvotes_.contains(user.getGoogleId_())) {
            upvotes_.remove(user.getGoogleId_());
            //one upvote less
            rating_ -= 1;
        }
        downvotes_.add(user.googleId_);
        //one downvote more
        rating_ -= 1;
        return;
    }
}
