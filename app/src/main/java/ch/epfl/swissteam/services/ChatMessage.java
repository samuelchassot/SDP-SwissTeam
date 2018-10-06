package ch.epfl.swissteam.services;

import java.util.Date;

/**
 * This class helps to use firebase to store messages
 *
 * @author Sébastien Gachoud
 */
public class ChatMessage {


    /**
     *
     * @param text text of the message
     * @param user name of the user who sent the message
     */
    public ChatMessage(String text, String user, String userId){
        text_ = text;
        user_ = user;
        userId_ = userId;
        time_ = new Date().getTime();
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

    public void setText_(String text) {
        text_ = text;
    }

    public void setTime_(long time) {
        time_ = time;
    }

    public void setUser_(String user) {
        this.user_ = user;
    }

    public void setUserId_(String userId) {
        userId_ = userId;
    }

    private String user_;
    private String userId_;
    private String text_;
    private long time_;

}
