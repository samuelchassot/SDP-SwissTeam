package ch.epfl.swissteam.services;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

/**
 * Class representing a search for service post.
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public class Post implements DBSavable, Parcelable{

    private String title_, googleId_, body_;
    private long timestamp_;
    private String key_;
    private Location location_;

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
     * @param location_ location of the post
     */
    public Post(String key_, String title_, String googleId_, String body_, long timestamp_, Location location_) {
        this.key_ = key_;
        this.title_ = title_;
        this.googleId_ = googleId_;
        this.body_ = body_;
        this.timestamp_ = timestamp_;
        this.location_ = location_
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

    public Location getLocation_() { return location_; }

    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    public void setBody_(String body_) {
        this.body_ = body_;
    }

    //Implements Parcelable
    public Post(Parcel in){
        String[] data= new String[5];

        in.readStringArray(data);
        this.key_= data[0];
        this.title_= data[1];
        this.googleId_= data[2];
        this.body_= data[3];
        this.timestamp_= Long.parseLong(data[4]);
        this.location_ = Location.CREATOR.createFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.key_,this.title_, this.googleId_, this.body_,
                String.valueOf(this.timestamp_)});
        location_.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<Post> CREATOR= new Parcelable.Creator<Post>() {

        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);  //using parcelable constructor
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
