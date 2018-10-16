package ch.epfl.swissteam.services;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

/**
 * Class representing a search for service post.
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public class Post implements DBSavable{

    private String title_, googleId_, body_;
    private long timestamp_;
    private String key_;

    /**
     * Default constructor required for database.
     */
    public Post() {

    }

    /**
     * Construct a post for searching services.
     *
     * @param title_ the title of the post
     * @param googleId_ the id of the person who post the post
     * @param body_ the body of the post
     * @param timestamp_ the timestamp at which the post was submitted
     */
    public Post(String key_, String title_, String googleId_, String body_, long timestamp_) {
        this.key_ = key_;
        this.title_ = title_;
        this.googleId_ = googleId_;
        this.body_ = body_;
        this.timestamp_ = timestamp_;
    }

    /**
     * Store a post in the Firebase database.
     * @param databaseReference
     */
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child(DBUtility.POSTS).child(key_).setValue(this);
    }

    public String getTitle_() {
        return title_;
    }

    public String getGoogleId_() {
        return googleId_;
    }

    public String getBody_() {
        return body_;
    }

    public long getTimestamp_() {
        return timestamp_;
    }

    public String getKey_() {
        return key_;
    }
}
