package ch.epfl.swissteam.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests for ChatMessage
 *
 */
public class ChatMessageTest {
    @Test
    public void objectCreationYieldExpectedValueOnGets() {
        String text = "We are the champions!";
        String user = "Jean Calvin";
        String userId = "Calvinet";
        ChatMessage message = new ChatMessage(text, user, userId);
        assertEquals(text, message.getText_());
        assertEquals(user, message.getUser_());
        assertEquals(userId, message.getUserId_());
    }

    @Test
    public void setValueGivesBackSameWhenGet() {
        String text = "We are the champions!";
        String user = "Jean Calvin";
        String userId = "Calvinet";
        long time = 1001;
        ChatMessage message = new ChatMessage("I'm white", "Polar Bear", "PaulBeer");
        message.setText_(text);
        message.setUser_(user);
        message.setUserId_(userId);
        message.setTime_(time);
        assertEquals(text, message.getText_());
        assertEquals(user, message.getUser_());
        assertEquals(userId, message.getUserId_());
        assertEquals(time, message.getTime_());
    }

}
