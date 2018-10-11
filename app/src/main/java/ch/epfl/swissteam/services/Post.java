package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

/**
 * Class representing a search for service post.
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public class Post implements DBSavable{

    private String title_, username_, body_;
    private long timestamp_;

    /**
     * Default constructor required for database.
     */
    public Post() {

    }

    /**
     * Construct a post for searching services.
     *
     * @param title_ the title of the post
     * @param username_ the username of the person who post the post
     * @param body_ the body of the post
     * @param timestamp_ the timestamp at which the post was submitted
     */
    public Post(String title_, String username_, String body_, long timestamp_) {
        this.title_ = title_;
        this.username_ = username_;
        this.body_ = body_;
        this.timestamp_ = timestamp_;
    }

    /**
     * Store a post in the Firebase database.
     * @param databaseReference
     */
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child(DBUtility.POSTS).push().setValue(this);
    }

    public String getTitle_() {
        return title_;
    }

    public String getUsername_() {
        return username_;
    }

    public String getBody_() {
        return body_;
    }

    public long getTimestamp_() {
        return timestamp_;
    }
}
