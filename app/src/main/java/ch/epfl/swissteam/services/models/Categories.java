package ch.epfl.swissteam.services.models;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.swissteam.services.providers.DBUtility;

/**
 * The enum for Categories representing the different kind of user's capabilities
 */
public enum Categories implements DBSavable {
    ALL,
    IC,
    MATHS,
    GARDENING,
    COOKING,
    DAILYLIFE,
    TRANSPORTATION,
    HOUSE,
    TEACHING,
    MECHANICS;

    private List<String> users_;


    Categories() {
        users_ = new ArrayList<>();
    }

    /**
     * Convert a category in String form into its enum form
     *
     * @param category a String corresponding to a category
     * @return the enum corresponding to category
     */
    public static Categories fromString(String category) {
        switch (category) {
            case "Computer":
                return IC;
            case "Maths":
                return MATHS;
            case "House":
                return HOUSE;
            case "Cooking":
                return COOKING;
            case "Teaching":
                return TEACHING;
            case "Gardening":
                return GARDENING;
            case "Mechanics":
                return MECHANICS;
            case "Daily Life":
                return DAILYLIFE;
            case "Transportation":
                return TRANSPORTATION;
            case "All":
                return ALL;
            default:
                return null;

        }
    }

    /**
     * Gives all the actual categories (all except ALL)
     *
     * @return the actual categories
     */
    public static Categories[] realCategories() {
        return Arrays.copyOfRange(Categories.values(), 1, Categories.values().length);
    }

    @Override
    public String toString() {
        switch (this) {
            case IC:
                return "Computer";
            case MATHS:
                return "Maths";
            case HOUSE:
                return "House";
            case COOKING:
                return "Cooking";
            case TEACHING:
                return "Teaching";
            case GARDENING:
                return "Gardening";
            case MECHANICS:
                return "Mechanics";
            case DAILYLIFE:
                return "Daily Life";
            case TRANSPORTATION:
                return "Transportation";
            case ALL:
                return "All";
        }
        return null;

    }

    /**
     * Add a user to the list of users
     *
     * @param user a user
     */
    public void addUser(User user) {
        addUser(user.getGoogleId_());
    }

    /**
     * Add a user to the list of users
     *
     * @param googleId the google ID of a user
     */
    public void addUser(String googleId) {
        users_.add(googleId);
    }

    /**
     * Remove a user from the list of users
     *
     * @param user a user
     */
    public void removeUser(User user) {
        removeUser(user.getGoogleId_());
    }

    /**
     * Remove a user from the list of users
     *
     * @param googleId the google ID of a user
     */
    public void removeUser(String googleId) {
        users_.remove(googleId);
    }

    /**
     * Give an unmodifiable view of the users
     *
     * @return the list of users
     */
    public List<String> getUsers_() {
        return Collections.unmodifiableList(users_);
    }

    @Override
    public void addToDB(DatabaseReference db) {
        if (this != ALL) {
            db.child(DBUtility.get().CATEGORIES).removeValue();
            for (String googleId : users_) {
                db.child(DBUtility.get().CATEGORIES).child(this.toString()).child(googleId).setValue("true");
            }
        } else {
            Log.e("Category", "Cannot save category 'all' to database");
        }
    }
}
