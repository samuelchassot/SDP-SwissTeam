package ch.epfl.swissteam.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class representing a search for service post.
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public class Post implements DBSavable, Parcelable {

    private String title_, googleId_, body_;
    private long timestamp_;
    private String key_;
    private double longitude_, latitude_;
    private String timeoutDateString_;

    /**
     * Default constructor required for database.
     */
    public Post() {

    }

    /**
     * Construct a post for searching services.
     *
     * @param title_     the title of the post
     * @param googleId_  the id of the person who posts the post
     * @param body_      the body of the post
     * @param timestamp_ the timestamp at which the post was submitted
     * @param longitude_ longitude of the location of the post
     * @param latitude_ latitude of the location of the post
     * @param timeoutDateString_ date after which the post is no more available (format "yyy-MM-dd")
     *                           if the format doesn't match, default value is today date + 6 mounths
     */
    public Post(String key_, String title_, String googleId_, String body_, long timestamp_, double longitude_, double latitude_, String timeoutDateString_) {
        this.key_ = key_;
        this.title_ = title_;
        this.googleId_ = googleId_;
        this.body_ = body_;
        this.timestamp_ = timestamp_;
        this.longitude_ = longitude_;
        this.latitude_ = latitude_;
        if(timeoutDateString_.matches("\\d{4}-\\d{2}-\\d{2}")){
            this.timeoutDateString_ = new String(timeoutDateString_);
        }else{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 6);
            this.timeoutDateString_ = dateToString(cal.getTime());
        }

    }

    //Implements Parcelable
    public Post(Parcel in){
        String[] data= new String[7];

        in.readStringArray(data);
        this.key_= data[0];
        this.title_= data[1];
        this.googleId_= data[2];
        this.body_= data[3];
        this.timestamp_= Long.parseLong(data[4]);
        this.longitude_ = Double.parseDouble(data[5]);
        this.latitude_ = Double.parseDouble(data[6]);
    }

    /**
     * Store a post in the Firebase database.
     *
     * @param databaseReference
     */
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child(DBUtility.POSTS).child(key_).setValue(this);
    }

    /**
     * Delete this post in the Firebase database
     * @param databaseReference
     */
    public void deleteFromDB(DatabaseReference databaseReference){
        databaseReference.child(DBUtility.POSTS).child(key_).removeValue();
    }

    /**
     * Given the today's date, delete the post if it is too old
     * @param today today's Date object
     * @param databaseReference the Firebase database reference
     * @return true if the post was deleted, false otherwise
     */
    public boolean deleteIfTooOld(Date today, DatabaseReference databaseReference){
        if(today != null && today.after(dateFromString(timeoutDateString_))){
            deleteFromDB(databaseReference);
            return true;
        }
        return false;
    }

    /**
     * Gives the title of the post
     *
     * @return the title of the post
     */
    public String getTitle_() {
        return title_;
    }

    /**
     * Set the title of the post
     *
     * @param title_ the title of the post
     */
    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    /**
     * Gives the google id of the user who posted the post
     *
     * @return the google id of the author
     */
    public String getGoogleId_() {
        return googleId_;
    }

    /**
     * Used when the author of the post delete his/her account, it changes the googleID to the "deleted user" one
     */
    public void removeUser(){
        googleId_ = User.getDeletedUserGoogleID();
        this.addToDB(DBUtility.get().getDb_());
    }

    /**
     * Gives the description of the post
     *
     * @return the description of the post
     */
    public String getBody_() {
        return body_;
    }

    /**
     * Set the description of the post
     *
     * @param body_ the description of the post
     */
    public void setBody_(String body_) {
        this.body_ = body_;
    }

    /**
     * Gives the time at which the post was posted
     *
     * @return the timestamp of the post
     */
    public long getTimestamp_() {
        return timestamp_;
    }

    /**
     * Gives the key of the post
     *
     * @return the key of the post
     */
    public String getKey_() {
        return key_;
    }

    /**
     * Gives the latitude of the location of the post
     *
     * @return the latitude of location of the post
     */
    public double getLatitude_() { 
      return latitude_; 
    }

    /**
     * Gives the longitude of the location of the post
     *
     * @return the longitude of location of the post
     */
    public double getLongitude_() { 
      return longitude_; 
    }

    public String getTimeoutDateString_() {
        return timeoutDateString_;
    }

    public void setTimeoutDateString_(String timeoutDateString_) {
        this.timeoutDateString_ = timeoutDateString_;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.key_,this.title_, this.googleId_, this.body_,
                String.valueOf(this.timestamp_), ((Double)this.longitude_).toString(), ((Double)this.latitude_).toString()});
    }

    /**
     * Transform a string into a date. String must have the format "yyyy-MM-dd"
     * @param dateString
     * @return a Date object (null if string's format is not the required)
     */
    public static Date dateFromString(String dateString){
        if(dateString == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Transform a Date object into a String with format "yyyy-MM-dd"
     * @param date the Date object to transform
     * @return the String in the required format (null if date == null)
     */
    public static String dateToString(Date date){
        if(date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        return format;
    }

    /**
     * Transform a Post into a {@link Parcelable}
     */
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {

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
