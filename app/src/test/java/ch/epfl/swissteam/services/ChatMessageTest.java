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
        assertEquals(message.getText_(), text);
        assertEquals(message.getUser_(), user);
        assertEquals(message.getUserId_(), userId);
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
        assertEquals(message.getText_(), text);
        assertEquals(message.getUser_(), user);
        assertEquals(message.getUserId_(), userId);
        assertEquals(message.getTime_(), time);
    }

}
