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
        String relationId = "-234";
        ChatMessage message = new ChatMessage(text, user, userId, relationId);
        assertEquals(text, message.getText_());
        assertEquals(user, message.getUser_());
        assertEquals(userId, message.getUserId_());
    }

    @Test
    public void setValueGivesBackSameWhenGet() {
        String text = "I'm white";
        String user = "Polar Bear";
        String userId = "PaulBeer";
        String relationId = "-234";
        long time = 1001;
        ChatMessage message = new ChatMessage();
        message.setText_(text);
        message.setUser_(user);
        message.setUserId_(userId);
        message.setTime_(time);
        message.setRelationId_(relationId);
        assertEquals(text, message.getText_());
        assertEquals(user, message.getUser_());
        assertEquals(userId, message.getUserId_());
        assertEquals(time, message.getTime_());
        assertEquals(relationId, message.getRelationId_());
    }

}
