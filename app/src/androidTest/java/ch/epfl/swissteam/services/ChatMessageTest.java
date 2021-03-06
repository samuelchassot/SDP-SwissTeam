package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.swissteam.services.models.ChatMessage;

import static org.junit.Assert.assertEquals;

/**
 * tests for ChatMessage
 *
 */
@RunWith(AndroidJUnit4.class)
public class ChatMessageTest {
    //constructor
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
        assertEquals(relationId, message.getRelationId_());
    }

    @Test
    public void defaultObjectCreationYieldExpectedValueOnGets() {
        ChatMessage message = new ChatMessage();
        assertEquals(null, message.getText_());
        assertEquals(null, message.getUser_());
        assertEquals(null, message.getUserId_());
        assertEquals(null, message.getRelationId_());
    }

    //setter and getters
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
