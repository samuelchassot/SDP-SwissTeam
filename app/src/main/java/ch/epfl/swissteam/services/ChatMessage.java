package ch.epfl.swissteam.services;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * This class helps to store messages in firebase
 *
 * @author Sébastien Gachoud
 */
public class ChatMessage implements DBSavable {

    private String user_;
    private String userId_;
    private String text_;
    private long time_;
    private String relationId_;

    /**
     * @param text       text of the message
     * @param user       name of the user who sent the message
     * @param userId     the id of the user
     * @param relationId the id of the relation of the chat
     */
    public ChatMessage(String text, String user, String userId, String relationId) {
        text_ = text;
        user_ = user;
        userId_ = userId;
        time_ = new Date().getTime();
        relationId_ = relationId;
    }

    public ChatMessage() {
    }

    /**
     * Gives the time when the message was sent
     *
     * @return the time of the message
     */
    public long getTime_() {
        return time_;
    }

    /**
     * Set the time of the message
     *
     * @param time the time of the message
     */
    public void setTime_(long time) {
        time_ = time;
    }

    /**
     * Gives the text of the message
     *
     * @return the text of the message
     */
    public String getText_() {
        return text_;
    }

    /**
     * Set the text of the message
     *
     * @param text the text of the message
     */
    public void setText_(String text) {
        text_ = text;
    }

    /**
     * Gives the ID of the user who sent the message
     *
     * @return the ID of the user
     */
    public String getUserId_() {
        return userId_;
    }

    /**
     * Set the ID of the user who sent the message
     *
     * @param userId the ID of the user
     */
    public void setUserId_(String userId) {
        userId_ = userId;
    }

    /**
     * Gives the name of the user who sent the message
     *
     * @return the name of the author of the message
     */
    public String getUser_() {
        return user_;
    }

    /**
     * Set the name of the user
     *
     * @param user the name of the author
     */
    public void setUser_(String user) {
        user_ = user;
    }

    /**
     * Gives the ID of the relation of the chat
     *
     * @return the ID of the user
     */
    public String getRelationId_() {
        return relationId_;
    }

    /**
     * Set the ID of the relation of the chat
     *
     * @param relationId the Id of the user
     */
    public void setRelationId_(String relationId) {
        relationId_ = relationId;
    }

    @Override
    public void addToDB(DatabaseReference databaseReference) {
        databaseReference.child(DBUtility.CHATS).child(relationId_).push().setValue(this);
    }

    /**
     * remove the message in databaseReference designed by key. Does nothing if key is null.
     * @param databaseReference The database reference of the target database
     * @param child   The id of the message in the database, if null nothing happens.
     */
    @Override
    public void removeFromDB(DatabaseReference databaseReference, String child){
        if(child != null)
            databaseReference.child(DBUtility.CHATS).child(relationId_).child(child).removeValue();
    }
}
