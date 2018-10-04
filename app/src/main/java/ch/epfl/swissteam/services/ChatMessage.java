package ch.epfl.swissteam.services;

import java.util.Date;

/**
 * This class helps to use firebase to store messages
 *
 * @author Sébastien Gachoud
 */
public class ChatMessage {
    private String user_;
    private String text_;
    private long time_;

    /**
     *
     * @param text text of the message
     * @param user name of the user who sent the message
     */
    public ChatMessage(String text, String user){
        text_ = text;
        user_ = user;
        time_ = new Date().getTime();
    }

    public ChatMessage(){}

    public long getTime() {
        return time_;
    }

    public String getText() {
        return text_;
    }

    public String getUser() {
        return user_;
    }

    public void setText(String text) {
        text_ = text;
    }

    public void setTime(long time) {
        time_ = time;
    }

    public void setUser(String user) {
        this.user_ = user;
    }
}
