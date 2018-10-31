package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * This class helps to store messages in firebase
 *
 * @author Sébastien Gachoud
 */
public class ChatMessage implements DBSavable{


    /**
     *
     * @param text text of the message
     * @param user name of the user who sent the message
     * @param userId the id of the user
     */
    public ChatMessage(String text, String user, String userId, String relationId){
        text_ = text;
        user_ = user;
        userId_ = userId;
        time_ = new Date().getTime();
        relationId_ = relationId;
    }

    public ChatMessage(){}

    public long getTime_() {
        return time_;
    }

    public String getText_() {
        return text_;
    }

    public String getUserId_() { return userId_; }

    public String getUser_() {
        return user_;
    }

    public String getRelationId_(){ return relationId_; }

    public void setText_(String text) {
        text_ = text;
    }

    public void setTime_(long time) {
        time_ = time;
    }

    public void setUser_(String user) {
        user_ = user;
    }

    public void setUserId_(String userId) {
        userId_ = userId;
    }

    public void setRelationId_(String relationId) { relationId_ = relationId; }

    private String user_;
    private String userId_;
    private String text_;
    private long time_;
    private String relationId_;

    @Override
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child(DBUtility.CHATS).child(relationId_).push().setValue(this);
    }

    /**
     * remove the message in databaseReference designed by key. Does nothing if key is null.
     * @param databaseReference The database reference of the target database
     * @param key   The id of the message in the database, if null nothing happens.
     */
    public void removeFromDB(DatabaseReference databaseReference, String key){
        if(key != null)
            databaseReference.child(DBUtility.CHATS).child(relationId_).child(key).removeValue();
    }
}
