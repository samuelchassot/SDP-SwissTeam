package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
     * @param title the title of the post
     * @param username the username of the person who post the post
     * @param body the body of the post
     * @param timestamp the timestamp at which the post was submitted
     */
    public Post(String title, String username, String body, long timestamp) {
        this.title_ = title;
        this.username_ = username;
        this.body_ = body;
        this.timestamp_ = timestamp;
    }

    /**
     * Store a post in the Firebase database.
     * @param databaseReference
     */
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child("Posts").push().setValue(this);
    }

    public String getTitle_() {
        return title_;
    }

    public String getUsername() {
        return username_;
    }

    public String getBody_() {
        return body_;
    }

    public long getTimestamp_() {
        return timestamp_;
    }
}
